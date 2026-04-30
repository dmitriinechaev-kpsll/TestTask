package com.example.archtst.controller.impl;

import com.example.archtst.constant.Urls;
import com.example.archtst.controller.UserController;
import com.example.archtst.dto.*;
import com.example.archtst.mapper.AddressDtoMapper;
import com.example.archtst.mapper.UserDtoMapper;
import com.example.archtst.model.AddressModel;
import com.example.archtst.model.UserModel;
import com.example.archtst.enums.RoleName;
import com.example.archtst.service.AddressService;
import com.example.archtst.service.RoleService;
import com.example.archtst.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final UserDtoMapper userMapper;
    private final AddressService addressService;
    private final AddressDtoMapper addressMapper;
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userDTO) {
        log.info("POST " + Urls.BASE_URL + " - Создание пользователя: {}", userDTO.getEmail());
        UserModel savedUser = userService.createUser(userMapper.toModel(userDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponseDTO(savedUser));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<UserModel> usersPage = userService.getAllUsers(pageable);
        Page<UserResponseDTO> responsePage = userMapper.pageToResponseDTO(usersPage);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping(Urls.BY_ID)
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        UserModel user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }

    @DeleteMapping(Urls.BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info("DELETE " + Urls.BASE_URL + "{} - Удаление пользователя", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(Urls.SEARCH)
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @Valid @RequestBody UserSearchRequestDTO request
    ) {
        UserSearchCriteria criteria = userMapper.toCriteria(request);

        Sort.Direction direction = Sort.Direction.fromString(request.getSortDir());
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(direction, request.getSortBy())
        );

        Page<UserModel> usersPage = userService.searchUsers(criteria, pageable);

        return ResponseEntity.ok(usersPage.map(userMapper::toResponseDTO));
    }

    @PostMapping(Urls.ADDRESSES)
    public ResponseEntity<AddressResponseDTO> addAddress(
            @PathVariable UUID id,
            @Valid @RequestBody AddressRequestDTO request)
    {
        AddressModel incomingModel = addressMapper.toModel(request);
        AddressModel result = addressService.addAddressToUser(id, incomingModel);
        AddressResponseDTO responseDTO = addressMapper.toResponseDTO(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<Page<AddressResponseDTO>> getUserAddresses(UUID id, Pageable pageable) {
        Page<AddressModel> addressPage = addressService.getUserAddresses(id, pageable);
        Page<AddressResponseDTO> responsePage = addressPage.map(addressMapper::toResponseDTO);
        return ResponseEntity.ok(responsePage);
    }

    @Override
    public ResponseEntity<Void> removeAddress(UUID id, UUID addressId) {
        addressService.removeAddress(id, addressId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserResponseDTO> addRole(
            @PathVariable UUID id,
            @Valid @RequestBody RoleRequestDTO request
    ) {
        UserModel updatedUser = roleService.addRoleToUser(id, request.getRoleName());
        return ResponseEntity.ok(userMapper.toResponseDTO(updatedUser));
    }

    @Override
    public ResponseEntity<UserResponseDTO> removeRole(UUID id, RoleName roleName) {
        UserModel updatedUser = roleService.removeRoleFromUser(id, roleName);
        return ResponseEntity.ok(userMapper.toResponseDTO(updatedUser));
    }
}