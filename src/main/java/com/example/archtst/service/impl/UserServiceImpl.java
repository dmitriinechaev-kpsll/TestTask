package com.example.archtst.service.impl;

import com.example.archtst.entity.User;
import com.example.archtst.exception.UserAlreadyExistException;
import com.example.archtst.exception.UserNotFoundException;
import com.example.archtst.repository.UserRepository;
import com.example.archtst.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User newUser) {
        log.info("Создание пользователя: {}", newUser.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(newUser.getEmail());
        if (optionalUser.isPresent()) {
            log.info("Такой пользователь уже существует!!");
            throw new UserAlreadyExistException("email: " + newUser.getEmail());
        }
        return userRepository.save(newUser);
    }

    @Override
    public User getUserByEmail(String email) {
        // log.info("Поиск пользователя по email: {}", email);
        //return userRepository.findByEmail(email);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        log.info("Такой пользователь уже существует!!");
        throw new UserNotFoundException("email: " + email);
    }

     @Override
     public Page<User> getAllUsers(Pageable pageable) {
         log.info("Получение всех пользователей (страница: {}, размер: {})",
                 pageable.getPageNumber(), pageable.getPageSize());
         return userRepository.findAll(pageable); // Возвращаем Page<User>
     }

    @Override
    public User getUserById(UUID id) {
        //log.info("Поиск пользователя по ID: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();//userRepository.findById(id);
        }
        throw new UserNotFoundException("id: " + id);
    }

    @Override
    public Page<User> searchUsersByName(String name, Pageable pageable) {
        log.info("Поиск пользователей по имени: {}", name);
        return userRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<User> getUsersOlderThan(Integer age, Pageable pageable) {
        log.info("Поиск пользователей старше: {}", age);
        return userRepository.findByAgeGreaterThan(age, pageable);
    }

    @Override
    public boolean deleteUser(UUID id) {
        User user = getUserById(id);
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public long getTotalUsersCount() {
        return userRepository.count();
    }
}