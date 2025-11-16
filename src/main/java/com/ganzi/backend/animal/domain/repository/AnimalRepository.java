package com.ganzi.backend.animal.domain.repository;

import com.ganzi.backend.animal.domain.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, String>, AnimalRepositoryCustom {
    Optional<Animal> findByDesertionNo(String desertionNo);
}
