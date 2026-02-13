package com.example.archtst.controller;

import com.example.archtst.dto.UserRequestDTO;
import com.example.archtst.dto.UserResponseDTO;
import com.example.archtst.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO userDTO) {
        log.info("POST /api/users - Создание пользователя: {}", userDTO.getEmail());

        UserResponseDTO createdUser = userService.createUser(userDTO);
        if (createdUser.getError() != null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", createdUser.getError());
            error.put("email", userDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("GET /api/users - Получение всех пользователей");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        log.info("GET /api/users/{} - Поиск пользователя", id);
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        log.info("DELETE /api/users/{} - Удаление пользователя", id);

        if (userService.deleteUser(id)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Пользователь успешно удален");
            response.put("id", id);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer olderThan) {

        log.info("GET /api/users/search - Поиск: email={}, name={}, olderThan={}",
                email, name, olderThan);

        if (email != null) {
            return userService.getUserByEmail(email)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        if (name != null) {
            return ResponseEntity.ok(userService.searchUsersByName(name));
        }

        if (olderThan != null) {
            return ResponseEntity.ok(userService.getUsersOlderThan(olderThan));
        }

        return ResponseEntity.badRequest().body("Укажите параметр поиска");
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        log.info("GET /api/users/stats - Статистика");

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getTotalUsersCount());
        stats.put("timestamp", java.time.LocalDateTime.now().toString());
        stats.put("database", "PostgreSQL");
        stats.put("appName", "Archtst API");

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("API is working! PostgreSQL connected.");
    }
}