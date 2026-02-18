package com.example.archtst.controller;

import com.example.archtst.constant.Urls;
import com.example.archtst.dto.RequestDTO;
import com.example.archtst.dto.ResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping(Urls.BASE_URL)
public interface UserController {

    @PostMapping
    ResponseEntity<?> createUser(@Valid @RequestBody RequestDTO userDTO);

    @GetMapping
    ResponseEntity<Page<ResponseDTO>> getAllUsers(Pageable pageable);
    //ResponseEntity<List<ResponseDTO>> getAllUsers();

    @GetMapping(Urls.BY_ID)
    ResponseEntity<?> getUserById(@PathVariable UUID id);

    @DeleteMapping(Urls.BY_ID)
    public void deleteUser(@PathVariable UUID id);

    @GetMapping(Urls.SEARCH)
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer olderThan,
            Pageable pageable);

    @GetMapping(Urls.STATS)
    ResponseEntity<Map<String, Object>> getStats();

    @GetMapping(Urls.HEALTH)
    public ResponseEntity<String> healthCheck();


}