package com.example.archtst.service;

import com.example.archtst.dto.RequestDTO;
import com.example.archtst.dto.ResponseDTO;
import com.example.archtst.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User newUser);
    List<User> getAllUsers();
    Optional<User> getUserById(String id);
    Optional<User> getUserByEmail(String email);
    List<User> searchUsersByName(String name);
    List<User> getUsersOlderThan(Integer age);
    boolean deleteUser(String id);
    long getTotalUsersCount();

 }
