package com.example.archtst.controller.impl;

import com.example.archtst.constant.Urls;
import com.example.archtst.controller.UserController;
import com.example.archtst.dto.*;
import com.example.archtst.entity.User;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userDTO) {
        log.info("POST " + Urls.BASE_URL + " - Создание пользователя: {}", userDTO.getEmail());
        User savedUser = userService.createUser(userMapper.requestToEntity(userDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToResponseDTO(savedUser));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        // log.info("GET " + Urls.BASE_URL + " - Получение всех пользователей с пагинацией");
        Page<User> usersPage = userService.getAllUsers(pageable);
        Page<UserResponseDTO> responsePage = userMapper.pageToResponseDTO(usersPage);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping(Urls.BY_ID)
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        // log.info("GET " + Urls.BASE_URL + "{} - Поиск пользователя", id);
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

    @PostMapping(Urls.SEARCH) // Например: "/api/users/search"
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @Valid @RequestBody UserSearchRequestDTO request
    ) {
        log.info("POST {} - Поиск пользователей. Фильтры: {}", Urls.SEARCH, request);

        // 1. Определяем направление сортировки
        Sort.Direction direction = request.getSortDir().equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        // 2. Создаем объект Pageable на основе данных из DTO
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(direction, request.getSortBy())
        );

        // 3. Вызываем сервис, передавая ему DTO с фильтрами и настройки страницы
        Page<User> usersPage = userService.searchUsers(request, pageable);

        // 4. Преобразуем Page<User> в Page<ResponseDTO>
        Page<UserResponseDTO> responsePage = userMapper.pageToResponseDTO(usersPage);

        return ResponseEntity.ok(responsePage);
    }

/*    @GetMapping(Urls.STATS)
    public ResponseEntity<Map<String, Object>> getStats() {
        log.info("GET " + Urls.BASE_URL + Urls.STATS + " - Статистика");

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getTotalUsersCount());
        stats.put("timestamp", java.time.LocalDateTime.now().toString());
        stats.put("database", "PostgreSQL");
        stats.put("appName", "Archtst API");

        return ResponseEntity.ok(stats);
    }*/

    // Добавление адреса пользователю
    @PostMapping(Urls.ADDRESSES) // /users/{id}/addresses
    public ResponseEntity<AddressResponseDTO> addAddress(
            @PathVariable UUID id,
            @Valid @RequestBody AddressRequestDTO request)
    {
        //AddressResponseDTO response = userService.addAddressToUser(id, request);
        AddressUpsertResult  result = userService.addAddressToUser(id, request);
        if (result.isCreated()){
            return ResponseEntity
                    .status(HttpStatus.CREATED) // Возвращаем 201 Created
                    .body(result.address());
        }else {
            // Если обновили -> 200 OK
            return ResponseEntity.ok(result.address());
        }
    }

    @Override
    public ResponseEntity<Page<AddressResponseDTO>> getUserAddresses(UUID id, Pageable pageable) {
        Page<AddressResponseDTO> addresses = userService.getUserAddresses(id, pageable);
        return ResponseEntity.ok(addresses);
    }

    @Override
    public ResponseEntity<Void> removeAddress(UUID id, UUID addressId) {
        userService.removeAddress(id, addressId);
        return ResponseEntity.noContent().build(); // Возвращает статус 204
    }

    @Override
    public ResponseEntity<UserResponseDTO> addRole(
            @PathVariable UUID id,
            @Valid @RequestBody RoleRequestDTO request
    ) {
        // Вызываем наш исправленный метод сервиса
        UserResponseDTO response = userService.addRoleToUser(id, request);

        // Возвращаем статус 200 OK и обновленного юзера в теле ответа
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponseDTO> removeRole(UUID id, String roleName) {
        UserResponseDTO response = userService.removeRoleFromUser(id, roleName);
        return ResponseEntity.ok(response);
    }
}