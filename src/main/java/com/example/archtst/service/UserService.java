package com.example.archtst.service;

import com.example.archtst.dto.UserSearchCriteria;
import com.example.archtst.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    UserModel createUser(UserModel newUser);
    Page<UserModel> getAllUsers(Pageable pageable);
    UserModel getUserById(UUID id);
    Page<UserModel> searchUsers(UserSearchCriteria criteria, Pageable pageable);
    UserModel saveUser(UserModel user);
    boolean deleteUser(UUID id);

}
