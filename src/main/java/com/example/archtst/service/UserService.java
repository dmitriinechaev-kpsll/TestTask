package com.example.archtst.service;

import com.example.archtst.dto.UserSearchCriteria;
import com.example.archtst.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    User createUser(User newUser);
    Page<User> getAllUsers(Pageable pageable);
    User getUserById(UUID id);
    Page<User> searchUsers(UserSearchCriteria criteria, Pageable pageable);
    User saveUser(User user);
    boolean deleteUser(UUID id);

}
