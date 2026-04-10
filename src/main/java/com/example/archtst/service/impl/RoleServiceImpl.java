package com.example.archtst.service.impl;

import com.example.archtst.entity.Role;
import com.example.archtst.exception.DuplicateResourceException;
import com.example.archtst.exception.ResourceNotFoundException;
import com.example.archtst.repository.RoleRepository;
import com.example.archtst.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Роль с именем '" + name + "' не найдена"));
    }

    @Override
    @Transactional
    public Role createRole(Role newRole) {
        if (roleRepository.existsByName(newRole.getName())) {
            throw new DuplicateResourceException("Роль с таким именем уже существует: " + newRole.getName());
        }
        return roleRepository.save(newRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}