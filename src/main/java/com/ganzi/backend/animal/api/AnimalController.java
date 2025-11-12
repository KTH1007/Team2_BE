package com.ganzi.backend.animal.api;

import com.ganzi.backend.animal.api.doc.AnimalControllerDoc;
import com.ganzi.backend.animal.api.dto.request.AnimalSearchRequest;
import com.ganzi.backend.animal.api.dto.response.AnimalDetailResponse;
import com.ganzi.backend.animal.api.dto.response.AnimalListResponse;
import com.ganzi.backend.animal.application.AnimalService;
import com.ganzi.backend.global.code.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/animals")
public class AnimalController implements AnimalControllerDoc {

    private final AnimalService animalService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AnimalListResponse>>> findAnimals(
            @ModelAttribute @Valid AnimalSearchRequest request,
            @PageableDefault(size = 20)
            Pageable pageable) {
        Page<AnimalListResponse> response = animalService.findAnimals(request, pageable);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Override
    @GetMapping("/{desertionNo}")
    public ResponseEntity<ApiResponse<AnimalDetailResponse>> findAnimalById(@PathVariable String desertionNo) {
        AnimalDetailResponse response = animalService.findAnimalById(desertionNo);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
