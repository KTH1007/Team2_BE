package com.ganzi.backend.animal.domain.repository;

import com.ganzi.backend.animal.domain.Animal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimalRepository extends JpaRepository<Animal, String>, AnimalRepositoryCustom {
    Optional<Animal> findByDesertionNo(String desertionNo);

    @Query("SELECT DISTINCT a.province FROM Animal a " +
            "WHERE a.province IS NOT NULL ORDER BY a.province")
    List<String> findDistinctProvinces();

    @Query("SELECT DISTINCT a.city FROM Animal a " +
            "WHERE (:province IS NULL OR a.province = :province) AND a.city IS NOT NULL ORDER BY a.city")
    List<String> findDistinctCitesByProvince(@Param("province") String province);
}
