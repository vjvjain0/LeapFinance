package com.leapfinance.repositories;

import com.leapfinance.models.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContentRepository extends JpaRepository<Content, UUID> {

    List<Content> findAllByUsername(String username);
}
