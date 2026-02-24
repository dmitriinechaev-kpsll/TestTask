package com.example.archtst.controller.impl;

import com.example.archtst.constant.Urls;
import com.example.archtst.controller.UserController;
import com.example.archtst.dto.UserSearchRequestDTO;
import com.example.archtst.entity.User;
import com.example.archtst.mapper.UserMapper;
import com.example.archtst.dto.RequestDTO;
import com.example.archtst.dto.ResponseDTO;
import com.example.archtst.service.UserService;
import com.example.archtst.repository.specification.UserSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public ResponseEntity<?> createUser(@Valid @RequestBody RequestDTO userDTO) {
        log.info("POST " + Urls.BASE_URL + " - Создание пользователя: {}", userDTO.getEmail());
        User savedUser = userService.createUser(userMapper.requestToEntity(userDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToResponseDTO(savedUser));
    }

    @GetMapping
    public ResponseEntity<Page<ResponseDTO>> getAllUsers(
            @PageableDefault(size = 20, page = 0) Pageable pageable) {
        // log.info("GET " + Urls.BASE_URL + " - Получение всех пользователей с пагинацией");
        Page<User> usersPage = userService.getAllUsers(pageable);
        Page<ResponseDTO> responsePage = userMapper.pageToResponseDTO(usersPage);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping(Urls.BY_ID)
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        // log.info("GET " + Urls.BASE_URL + "{} - Поиск пользователя", id);
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.userToResponseDTO(user));
    }

    @DeleteMapping(Urls.BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT) // Явно возвращаем 204
    public void deleteUser(@PathVariable UUID id) {
        log.info("DELETE " + Urls.BASE_URL + "{} - Удаление пользователя", id);
        userService.deleteUser(id);
    }

    @PostMapping(Urls.SEARCH) // Например: "/api/users/search"
    public ResponseEntity<Page<ResponseDTO>> searchUsers(
            @RequestBody UserSearchRequestDTO request
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
        Page<ResponseDTO> responsePage = userMapper.pageToResponseDTO(usersPage);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping(Urls.STATS)
    public ResponseEntity<Map<String, Object>> getStats() {
        log.info("GET " + Urls.BASE_URL + Urls.STATS + " - Статистика");

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getTotalUsersCount());
        stats.put("timestamp", java.time.LocalDateTime.now().toString());
        stats.put("database", "PostgreSQL");
        stats.put("appName", "Archtst API");

        return ResponseEntity.ok(stats);
    }

    /*@GetMapping(Urls.HEALTH)
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("API is working! PostgreSQL connected.");
    }*/
}