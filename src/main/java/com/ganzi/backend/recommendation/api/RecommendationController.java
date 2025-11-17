package com.ganzi.backend.recommendation.api;

import com.ganzi.backend.animal.api.dto.response.AnimalDetailResponse;
import com.ganzi.backend.animal.application.AnimalService;
import com.ganzi.backend.global.code.dto.ApiResponse;
import com.ganzi.backend.global.security.userdetails.CustomUserDetails;
import com.ganzi.backend.recommendation.api.doc.RecommendationControllerDoc;
import com.ganzi.backend.recommendation.application.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController implements RecommendationControllerDoc {

    private final RecommendationService recommendationService;
    private final AnimalService animalService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AnimalDetailResponse>>> recommendAnimals(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "top", defaultValue = "8") Integer top
    ) {
        Long userId = userDetails.getUser().getId();

        List<String> desertionNos = recommendationService.recommend(userId, top);
        List<AnimalDetailResponse> animals = desertionNos.stream()
                .map(animalService::findAnimalById)
                .toList();

        return ResponseEntity.ok(ApiResponse.onSuccess(animals));
    }
}