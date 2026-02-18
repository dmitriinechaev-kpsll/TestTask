package com.example.archtst.controller.impl;

import com.example.archtst.constant.Urls;
import com.example.archtst.controller.UserController;
import com.example.archtst.entity.User;
import com.example.archtst.mapper.UserMapper;
import com.example.archtst.dto.RequestDTO;
import com.example.archtst.dto.ResponseDTO;
import com.example.archtst.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        /*try{
            User savedUser = userService.createUser(newUser);
            ResponseDTO resp = userMapper.userToResponseDTO(savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            log.error("Ошибка создания пользователя: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка: " + e.getMessage() );
            error.put("email", userDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }*/
        /*ResponseDTO createdUser = userService.createUser(userDTO);
        if (createdUser.getError() != null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", createdUser.getError());
            error.put("email", userDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        // */
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

    @GetMapping(Urls.SEARCH)
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer olderThan,
            Pageable pageable) {

        log.info("GET " + Urls.BASE_URL + Urls.SEARCH + " - Поиск: email={}, name={}, olderThan={}",
                email, name, olderThan);

        if (email != null) {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(userMapper.userToResponseDTO(user));
        }

        if (name != null) {
            Page<User> usersPage = userService.searchUsersByName(name, pageable);
            Page<ResponseDTO> responsePage = userMapper.pageToResponseDTO(usersPage);
            return ResponseEntity.ok(responsePage);
        }

        if (olderThan != null) {
            Page<User> usersPage = userService.getUsersOlderThan(olderThan, pageable);
            Page<ResponseDTO> responsePage = userMapper.pageToResponseDTO(usersPage);
            return ResponseEntity.ok(responsePage);
        }

        return ResponseEntity.badRequest().body("Укажите параметр поиска");
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

    @GetMapping(Urls.HEALTH)
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("API is working! PostgreSQL connected.");
    }
}