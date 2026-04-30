package com.example.archtst.persistence.repository;

import com.example.archtst.model.RoleModel;
import com.example.archtst.persistence.entity.Role;
import com.example.archtst.enums.RoleName;
import com.example.archtst.persistence.mapper.RoleEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RolePersistenceService {

    private final RoleRepository roleRepository;
    private final RoleEntityMapper mapper;

    public Optional<RoleModel> findByName(RoleName name) {
        return roleRepository.findByName(name).map(mapper::toModel);
    }

    public Role findEntityByName(RoleName name) {
        return roleRepository.findByName(name).orElse(null);
    }
}