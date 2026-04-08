package com.example.archtst.repository;

import com.example.archtst.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User>
{
    // 1. Этот метод чинит твой getAllUsers (без фильтров)
    @Override
    @EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
    Page<User> findAll(Pageable pageable);

    // 2. А ЭТОТ метод чинит твой searchUsers (со Specification)
    @Override
    @EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
    Page<User> findAll(@Nullable Specification<User> spec, Pageable pageable);

    //@Override
    //Optional<User> findById(String s);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<User> findByAgeGreaterThan(Integer age, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(@Param("domain") String domain);

    @Query(value = "SELECT * FROM users WHERE age > :minAge ORDER BY created_at DESC",
            nativeQuery = true)
    List<User> findUsersOlderThan(@Param("minAge") Integer minAge);
}