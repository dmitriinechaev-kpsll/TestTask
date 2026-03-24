package com.example.archtst.controller;

import com.example.archtst.constant.Urls;
import com.example.archtst.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userDTO);

    @PostMapping(Urls.SEARCH)
    ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @RequestBody UserSearchRequestDTO request
    );

    @GetMapping
    ResponseEntity<Page<UserResponseDTO>> getAllUsers(Pageable pageable);

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

    @Operation(summary = "Назначить роль пользователю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль успешно назначена"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или роль не существует в БД")
    })
    @PostMapping(Urls.ROLES)
    ResponseEntity<UserResponseDTO> addRole(
            @PathVariable UUID id,
            @Valid @RequestBody RoleRequestDTO request
    );

}