package com.jwt.implementation.repository;

import com.jwt.implementation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long userId);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}

