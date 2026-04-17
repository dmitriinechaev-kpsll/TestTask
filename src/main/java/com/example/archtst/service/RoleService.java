package com.example.archtst.service;

import com.example.archtst.entity.Role;
import com.example.archtst.enums.RoleName;

public interface RoleService {
    Role getRoleByName(RoleName name);
}
