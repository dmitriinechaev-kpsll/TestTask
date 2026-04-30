package com.example.archtst.service.impl;

import com.example.archtst.enums.RoleName;
import com.example.archtst.exception.ResourceNotFoundException;
import com.example.archtst.model.RoleModel;
import com.example.archtst.model.UserModel;
import com.example.archtst.persistence.repository.RolePersistenceService;
import com.example.archtst.service.RoleService;
import com.example.archtst.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UserService userService;
    private final RolePersistenceService rolePersistenceService;

    @Override
    @Transactional(readOnly = true)
    public RoleModel getRoleByName(RoleName name) {
        return rolePersistenceService.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Роль с именем '" + name + "' не найдена"));
    }

    @Override
    @Transactional
    public UserModel addRoleToUser(UUID userId, RoleName roleName) {
        UserModel user = userService.getUserById(userId);
        RoleModel role = getRoleByName(roleName);
        user.addRole(role);
        return userService.saveUser(user);
    }

    @Override
    @Transactional
    public UserModel removeRoleFromUser(UUID userId, RoleName roleName) {
        UserModel user = userService.getUserById(userId);
        RoleModel role = getRoleByName(roleName);
        user.removeRole(role);
        return userService.saveUser(user);
    }
}