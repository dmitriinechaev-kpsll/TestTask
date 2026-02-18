package com.example.archtst.repository;

import com.example.archtst.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Page<User> findAll(Pageable pageable);

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