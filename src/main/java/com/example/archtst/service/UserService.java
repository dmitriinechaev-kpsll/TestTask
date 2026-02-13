package com.example.archtst.service;

import com.example.archtst.dto.UserRequestDTO;
import com.example.archtst.dto.UserResponseDTO;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userDTO);
    List<UserResponseDTO> getAllUsers();
    Optional<UserResponseDTO> getUserById(String id);
    Optional<UserResponseDTO> getUserByEmail(String email);
    List<UserResponseDTO> searchUsersByName(String name);
    List<UserResponseDTO> getUsersOlderThan(Integer age);
    boolean deleteUser(String id);
    boolean existsByEmail(String email);
    long getTotalUsersCount();
}
