package com.example.archtst.facade;

import com.example.archtst.entity.Role;
import com.example.archtst.entity.User;
import com.example.archtst.service.RoleService;
import com.example.archtst.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRoleFacade {

    private final UserService userService;
    private final RoleService roleService;

    @Transactional
    public User addRoleToUser(UUID userId, String roleName) {
        User user = userService.getUserById(userId);
        Role role = roleService.getRoleByName(roleName);
        user.addRole(role);
        return userService.saveUser(user);
    }

    @Transactional
    public User removeRoleFromUser(UUID userId, String roleName) {
        User user = userService.getUserById(userId);
        Role role = roleService.getRoleByName(roleName);
        user.removeRole(role);
        return userService.saveUser(user);
    }
}