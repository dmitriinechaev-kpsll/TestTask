package com.example.archtst.controller;

import com.example.archtst.constant.Urls;
import com.example.archtst.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequestMapping(Urls.BASE_URL)
public interface UserController {

    @PostMapping
    ResponseEntity<ResponseDTO> createUser(@Valid @RequestBody RequestDTO userDTO);

    @PostMapping(Urls.SEARCH)
    ResponseEntity<Page<ResponseDTO>> searchUsers(
            @RequestBody UserSearchRequestDTO request
    );

    @GetMapping
    ResponseEntity<Page<ResponseDTO>> getAllUsers(Pageable pageable);

    @GetMapping(Urls.BY_ID)
    ResponseEntity<?> getUserById(@PathVariable UUID id);

    @DeleteMapping(Urls.BY_ID)
    ResponseEntity<Void> deleteUser(@PathVariable UUID id);

    @GetMapping(Urls.STATS)
    ResponseEntity<Map<String, Object>> getStats();

    // Добавление адреса пользователю
    @PostMapping(Urls.ADDRESSES) // /users/{id}/addresses
    ResponseEntity<AddressResponseDTO> addAddress(
            @PathVariable UUID id,
            @Valid @RequestBody AddressRequestDTO request);

}