package com.example.archtst.controller.impl;

import com.example.archtst.constant.Urls;
import com.example.archtst.controller.UserController;
import com.example.archtst.dto.*;
import com.example.archtst.entity.Address;
import com.example.archtst.entity.User;
import com.example.archtst.enums.RoleName;
import com.example.archtst.facade.UserAddressFacade;
import com.example.archtst.facade.UserRoleFacade;
import com.example.archtst.mapper.AddressMapper;
import com.example.archtst.mapper.UserMapper;
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
    private final UserMapper userMapper;
    private final UserAddressFacade userAddressFacade;
    private final AddressMapper addressMapper;
    private final UserRoleFacade userRoleFacade;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userDTO) {
        log.info("POST " + Urls.BASE_URL + " - Создание пользователя: {}", userDTO.getEmail());
        User savedUser = userService.createUser(userMapper.requestToEntity(userDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToResponseDTO(savedUser));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<User> usersPage = userService.getAllUsers(pageable);
        Page<UserResponseDTO> responsePage = userMapper.pageToResponseDTO(usersPage);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping(Urls.BY_ID)
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.userToResponseDTO(user));
    }

    @DeleteMapping(Urls.BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT) // Явно возвращаем 204
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info("DELETE " + Urls.BASE_URL + "{} - Удаление пользователя", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Возвращает 204
    }

    @PostMapping(Urls.SEARCH) // "/api/users/search"
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

        Page<User> usersPage = userService.searchUsers(criteria, pageable);

        return ResponseEntity.ok(usersPage.map(userMapper::userToResponseDTO));
    }

    @PostMapping(Urls.ADDRESSES) // /users/{id}/addresses
    public ResponseEntity<AddressResponseDTO> addAddress(
            @PathVariable UUID id,
            @Valid @RequestBody AddressRequestDTO request)
    {
        Address incomingAddress = addressMapper.toEntity(request);
        UserAddressFacade.AddressOperationResult result = userAddressFacade.addAddressToUser(id, incomingAddress);
        AddressResponseDTO responseDTO = addressMapper.toResponseDTO(result.address());
        if (result.isCreated()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        }
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<Page<AddressResponseDTO>> getUserAddresses(UUID id, Pageable pageable) {
        Page<Address> addressPage = userAddressFacade.getUserAddresses(id, pageable);
        Page<AddressResponseDTO> responsePage = addressPage.map(addressMapper::toResponseDTO);
        return ResponseEntity.ok(responsePage);
    }

    @Override
    public ResponseEntity<Void> removeAddress(UUID id, UUID addressId) {
        userAddressFacade.removeAddress(id, addressId);
        return ResponseEntity.noContent().build(); //  Returns status 204
    }

    @Override
    public ResponseEntity<UserResponseDTO> addRole(
            @PathVariable UUID id,
            @Valid @RequestBody RoleRequestDTO request
    ) {
        User updatedUser = userRoleFacade.addRoleToUser(id, request.getRoleName());
        return ResponseEntity.ok(userMapper.userToResponseDTO(updatedUser));
    }

    @Override
    public ResponseEntity<UserResponseDTO> removeRole(UUID id, RoleName roleName) {
        User updatedUser = userRoleFacade.removeRoleFromUser(id, roleName);
        return ResponseEntity.ok(userMapper.userToResponseDTO(updatedUser));
    }
}