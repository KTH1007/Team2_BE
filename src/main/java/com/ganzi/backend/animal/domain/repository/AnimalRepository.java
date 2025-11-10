package com.ganzi.backend.animal.domain.repository;

import com.ganzi.backend.animal.domain.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, String>, AnimalRepositoryCustom {
}
