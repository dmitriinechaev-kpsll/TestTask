package com.example.archtst.persistence.repository;

import com.example.archtst.persistence.entity.User;
import com.example.archtst.persistence.projection.UserWithRolesProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "SELECT u.id as id, u.name as name, u.email as email, u.age as age, " +
            "u.shoe_size as shoe_size, " +
            "COALESCE(string_agg(r.name, ','), '') as roles_string " +
            "FROM users u " +
            "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
            "LEFT JOIN roles r ON r.id = ur.role_id " +
            "WHERE u.deleted = false " +
            "GROUP BY u.id, u.name, u.email, u.age, u.shoe_size " +
            "ORDER BY u.created_at DESC",
            countQuery = "SELECT count(*) FROM users u WHERE u.deleted = false",
            nativeQuery = true)
    Page<UserWithRolesProjection> findAllWithRoles(Pageable pageable);
}