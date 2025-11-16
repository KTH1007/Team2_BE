package com.ganzi.backend.user.domain.repository;

import com.ganzi.backend.user.domain.UserEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEmbeddingRepository extends JpaRepository<UserEmbedding, Long> {
    Optional<UserEmbedding> findByUserId(Long userId);
}
