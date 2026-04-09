package com.example.archtst.service;

import com.example.archtst.dto.*;
import com.example.archtst.entity.Address;
import com.example.archtst.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface UserService {

    AddressUpsertResult addAddressToUser(UUID userId, Address incomingAddress);
    Page<AddressResponseDTO> getUserAddresses(UUID userId, Pageable pageable);
    void removeAddress(UUID userId, UUID addressId);

    User createUser(User newUser);
    Page<User> getAllUsers(Pageable pageable);
    User getUserById(UUID id);
    Page<User> searchUsers(UserSearchRequestDTO request, Pageable pageable);
    boolean deleteUser(UUID id);
    UserResponseDTO addRoleToUser(UUID userId, RoleRequestDTO request);
    UserResponseDTO removeRoleFromUser(UUID userId, String roleName);
}
