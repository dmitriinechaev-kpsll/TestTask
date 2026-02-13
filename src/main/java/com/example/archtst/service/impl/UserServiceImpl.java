package com.example.archtst.service.impl;

import com.example.archtst.dto.UserRequestDTO;
import com.example.archtst.dto.UserResponseDTO;
import com.example.archtst.entity.User;
import com.example.archtst.repository.UserRepository;
import com.example.archtst.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        log.info("Создание пользователя: {}", userDTO.getEmail());

        if (userRepository.existsByEmail(userDTO.getEmail())){
            UserResponseDTO respDto = new UserResponseDTO();
            respDto.setError("Email уже зарегистрирован");
            respDto.setEmail(userDTO.getEmail());
            return respDto;
        }

        try {
            User user = User.builder()
                    .name(userDTO.getName())
                    .email(userDTO.getEmail())
                    .age(userDTO.getAge())
                    .build();

            User savedUser = userRepository.save(user);
            log.info("Пользователь создан с ID: {}", savedUser.getId());
            return UserResponseDTO.fromEntity(savedUser);
        } catch (Exception e) {
            log.error("Ошибка создания пользователя: {}", e.getMessage());
            UserResponseDTO respDto = new UserResponseDTO();
            respDto.setError("Ошибка создания пользователя: "+e.getMessage());
            respDto.setEmail(userDTO.getEmail());
            return respDto;
        }
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        log.info("Получение всех пользователей");
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponseDTO> getUserById(String id) {
        log.info("Поиск пользователя по ID: {}", id);
        return userRepository.findById(id)
                .map(UserResponseDTO::fromEntity);
    }

    @Override
    public Optional<UserResponseDTO> getUserByEmail(String email) {
        log.info("Поиск пользователя по email: {}", email);
        return userRepository.findByEmail(email)
                .map(UserResponseDTO::fromEntity);
    }

    @Override
    public List<UserResponseDTO> searchUsersByName(String name) {
        log.info("Поиск пользователей по имени: {}", name);
        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getUsersOlderThan(Integer age) {
        log.info("Поиск пользователей старше: {}", age);
        return userRepository.findByAgeGreaterThan(age)
                .stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteUser(String id) {
        log.info("Удаление пользователя: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("Пользователь удален: {}", id);
            return true;
        }
        log.warn("Пользователь не найден: {}", id);
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public long getTotalUsersCount() {
        return userRepository.count();
    }
}