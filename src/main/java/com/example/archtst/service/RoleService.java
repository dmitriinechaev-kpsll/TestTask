package com.example.archtst.service;

import com.example.archtst.entity.Role;

import java.util.List;

public interface RoleService {
    Role getRoleByName(String name);
    Role createRole(Role role);
    List<Role> getAllRoles();
}
