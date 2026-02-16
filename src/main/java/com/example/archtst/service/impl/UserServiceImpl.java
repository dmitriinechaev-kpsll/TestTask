package com.example.archtst.service.impl;

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
    public User createUser(User newUser) {
        log.info("Создание пользователя: {}", newUser.getEmail());
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            log.info("Такой пользователь уже существует!!");
            throw new RuntimeException("Такой пользователь уже существует!!");
            //return userRepository.findByEmail(newUser.getEmail()).get();
        }
        return userRepository.save(newUser);
    /*    if (userRepository.existsByEmail(userDTO.getEmail())){
            ResponseDTO respDto = new ResponseDTO();
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
            return ResponseDTO.fromEntity(savedUser);
        } catch (Exception e) {
            log.error("Ошибка создания пользователя: {}", e.getMessage());
            ResponseDTO respDto = new ResponseDTO();
            respDto.setError("Ошибка создания пользователя: "+e.getMessage());
            respDto.setEmail(userDTO.getEmail());
            return respDto;
        }*/
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получение всех пользователей");
        List<User> tmp = userRepository.findAll();
        return tmp;
    }

    @Override
    public Optional<User> getUserById(String id) {
        log.info("Поиск пользователя по ID: {}", id);
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        log.info("Поиск пользователя по email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> searchUsersByName(String name) {
        log.info("Поиск пользователей по имени: {}", name);
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<User> getUsersOlderThan(Integer age) {
        log.info("Поиск пользователей старше: {}", age);
        return userRepository.findByAgeGreaterThan(age);
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
    public long getTotalUsersCount() {
        return userRepository.count();
    }
}