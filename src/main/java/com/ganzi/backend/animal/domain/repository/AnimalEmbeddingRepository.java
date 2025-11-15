package com.ganzi.backend.animal.domain.repository;

import com.ganzi.backend.animal.domain.AnimalEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalEmbeddingRepository extends JpaRepository<AnimalEmbedding, String> {
}
