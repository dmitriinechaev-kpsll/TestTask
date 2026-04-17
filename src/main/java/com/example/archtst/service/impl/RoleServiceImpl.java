package com.example.archtst.service.impl;

import com.example.archtst.entity.Role;
import com.example.archtst.enums.RoleName;
import com.example.archtst.exception.ResourceNotFoundException;
import com.example.archtst.repository.RoleRepository;
import com.example.archtst.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByName(RoleName name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Роль с именем '" + name + "' не найдена"));
    }
}