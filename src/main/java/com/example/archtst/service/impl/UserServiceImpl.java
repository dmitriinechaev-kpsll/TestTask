package com.example.archtst.service.impl;

import com.example.archtst.dto.UserSearchCriteria;
import com.example.archtst.exception.UserAlreadyExistException;
import com.example.archtst.exception.UserNotFoundException;
import com.example.archtst.model.UserModel;
import com.example.archtst.persistence.projection.UserWithRolesProjection;
import com.example.archtst.persistence.repository.UserPersistenceService;
import com.example.archtst.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserPersistenceService userPersistenceService;

    @Override
    public UserModel createUser(UserModel newUser) {
        log.info("Создание пользователя: {}", newUser.getEmail());
        var optionalUser = userPersistenceService.findByEmail(newUser.getEmail());
        if (optionalUser.isPresent()) {
            log.info("Такой пользователь уже существует!!");
            throw new UserAlreadyExistException("email: " + newUser.getEmail());
        }
        return userPersistenceService.save(newUser);
    }


    @Override
    public Page<UserWithRolesProjection> getAllUsers(Pageable pageable) {
        log.info("Получение всех пользователей (страница: {}, размер: {})",
                pageable.getPageNumber(), pageable.getPageSize());
        return userPersistenceService.findAllWithRoles(pageable);
    }

    @Override
    public Page<UserModel> searchUsers(UserSearchCriteria criteria, Pageable pageable) {
        return userPersistenceService.search(criteria, pageable);
    }

    @Override
    public UserModel getUserById(UUID id) {
        return userPersistenceService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id: " + id));
    }

    @Override
    public boolean deleteUser(UUID id) {
        UserModel user = getUserById(id);
        user.getUserRoles().clear();
        var entity = userPersistenceService.findEntityById(id);
        userPersistenceService.delete(entity);
        log.info("Пользователь {} и его связи с ролями успешно удалены", user.getEmail());
        return true;
    }

    @Override
    @Transactional
    public UserModel saveUser(UserModel user) {
        return userPersistenceService.save(user);
    }
}