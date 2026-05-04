package com.example.archtst.service;

import com.example.archtst.model.RoleModel;
import com.example.archtst.enums.RoleName;
import com.example.archtst.model.UserModel;

import java.util.UUID;

public interface RoleService {
    RoleModel getRoleByName(RoleName name);
    UserModel addRoleToUser(UUID userId, RoleName roleName);
    UserModel removeRoleFromUser(UUID userId, RoleName roleName);
}
