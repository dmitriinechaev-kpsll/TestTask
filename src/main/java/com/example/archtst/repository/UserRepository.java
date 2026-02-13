package com.example.archtst.repository;

import com.example.archtst.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByNameContainingIgnoreCase(String name);

    List<User> findByAgeGreaterThan(Integer age);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(@Param("domain") String domain);

    @Query(value = "SELECT * FROM users WHERE age > :minAge ORDER BY created_at DESC",
            nativeQuery = true)
    List<User> findUsersOlderThan(@Param("minAge") Integer minAge);
}