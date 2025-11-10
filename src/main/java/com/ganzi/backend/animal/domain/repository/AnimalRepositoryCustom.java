package com.ganzi.backend.animal.domain.repository;

import com.ganzi.backend.animal.api.dto.request.AnimalSearchRequest;
import com.ganzi.backend.animal.domain.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnimalRepositoryCustom {
    Page<Animal> searchWithFilters(AnimalSearchRequest request, Pageable pageable);
}
