package com.example.archtst.persistence.repository;

import com.example.archtst.model.UserModel;
import com.example.archtst.persistence.entity.User;
import com.example.archtst.persistence.mapper.UserEntityMapper;
import com.example.archtst.persistence.specification.UserSpecification;
import com.example.archtst.dto.UserSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPersistenceService {

    private final UserRepository userRepository;
    private final UserEntityMapper mapper;

    public UserModel save(UserModel model) {
        User entity = mapper.toEntity(model);
        return mapper.toModel(userRepository.save(entity));
    }

    public Optional<UserModel> findById(UUID id) {
        return userRepository.findById(id).map(mapper::toModel);
    }

    public Page<UserModel> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(mapper::toModel);
    }

    public Page<UserModel> search(UserSearchCriteria criteria, Pageable pageable) {
        Specification<User> spec = UserSpecification.byCriteria(criteria);
        return userRepository.findAll(spec, pageable).map(mapper::toModel);
    }

    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email).map(mapper::toModel);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User findEntityById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> findEntityByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}