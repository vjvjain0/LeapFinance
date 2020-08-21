package com.leapfinance.repositories;

import com.leapfinance.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
        User findByUsername(String username);

        Long countByUsername(String username);
}