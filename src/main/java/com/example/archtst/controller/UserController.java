package com.example.archtst.controller;

import com.example.archtst.dto.RequestDTO;
import com.example.archtst.dto.ResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/users")
public interface UserController {

    @PostMapping
    ResponseEntity<?> createUser(@Valid @RequestBody RequestDTO userDTO);

    @GetMapping
    ResponseEntity<List<ResponseDTO>> getAllUsers();

    @GetMapping("/{id}")
    ResponseEntity<?> getUserById(@PathVariable String id);

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id);

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer olderThan);

    @GetMapping("/stats")
    ResponseEntity<Map<String, Object>> getStats();

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck();


}