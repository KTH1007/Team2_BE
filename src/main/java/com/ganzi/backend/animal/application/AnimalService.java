package com.ganzi.backend.animal.application;

import com.ganzi.backend.animal.api.dto.request.AnimalSearchRequest;
import com.ganzi.backend.animal.api.dto.response.AnimalDetailResponse;
import com.ganzi.backend.animal.api.dto.response.AnimalListResponse;
import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.repository.AnimalRepository;
import com.ganzi.backend.global.code.status.ErrorStatus;
import com.ganzi.backend.global.exception.GeneralException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalService {

    private final AnimalRepository animalRepository;

    public Page<AnimalListResponse> findAnimals(AnimalSearchRequest request, Pageable pageable) {
        Page<Animal> animals = animalRepository.searchWithFilters(request, pageable);
        return animals.map(AnimalListResponse::from);
    }

    public AnimalDetailResponse findAnimalById(String desertionNo) {
        Animal animal = animalRepository.findById(desertionNo)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ANIMAL_NOT_FOUND));
        return AnimalDetailResponse.from(animal);
    }
}
