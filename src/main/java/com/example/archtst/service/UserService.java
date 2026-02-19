package com.example.archtst.service;

import com.example.archtst.dto.RequestDTO;
import com.example.archtst.dto.ResponseDTO;
import com.example.archtst.dto.UserSearchRequestDTO;
import com.example.archtst.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(User newUser);
    Page<User> getAllUsers(Pageable pageable);
    User getUserById(UUID id);
//    User getUserByEmail(String email);
//    Page<User> searchUsersByName(String name, Pageable pageable);
//    Page<User> getUsersOlderThan(Integer age, Pageable pageable);
    Page<User> searchUsers(UserSearchRequestDTO request, Pageable pageable);
    boolean deleteUser(UUID id);
    long getTotalUsersCount();
 }
