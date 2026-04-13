package com.example.archtst.service.impl;

import com.example.archtst.dto.UserSearchCriteria;
import com.example.archtst.entity.User;
import com.example.archtst.exception.UserAlreadyExistException;
import com.example.archtst.exception.UserNotFoundException;
import com.example.archtst.repository.UserRepository;
import com.example.archtst.repository.specification.UserSpecification;
import com.example.archtst.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
     public Page<User> getAllUsers(Pageable pageable) {
         log.info("Получение всех пользователей (страница: {}, размер: {})",
                 pageable.getPageNumber(), pageable.getPageSize());
         return userRepository.findAll(pageable); // Возвращаем Page<User>
     }

    @Override
    public Page<User> searchUsers(UserSearchCriteria criteria, Pageable pageable) {
        Specification<User> spec = UserSpecification.byCriteria(criteria);
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException("id: " + id));
    }

    @Override
    public boolean deleteUser(UUID id) {
        User user = getUserById(id);
        user.getUserRoles().clear();
        userRepository.delete(user);
        log.info("Пользователь {} и его связи с ролями успешно удалены", user.getEmail());
        return true;
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}