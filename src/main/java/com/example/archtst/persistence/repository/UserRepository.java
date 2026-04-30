package com.example.archtst.persistence.repository;

import com.example.archtst.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User>
{
    @NonNull
    @Override
    @EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
    Optional<User> findById(@NonNull UUID id);

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
    Page<User> findAll(@NonNull Pageable pageable);

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
    Page<User> findAll(@Nullable Specification<User> spec, @NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
    Optional<User> findByEmail(String email);
}