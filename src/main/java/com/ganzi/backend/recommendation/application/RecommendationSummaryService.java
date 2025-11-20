package com.ganzi.backend.recommendation.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.repository.AnimalRepository;
import com.ganzi.backend.global.code.status.ErrorStatus;
import com.ganzi.backend.global.exception.GeneralException;
import com.ganzi.backend.recommendation.infrastructure.UserInterestPromptFactory;
import com.ganzi.backend.recommendation.infrastructure.client.UpstageChatClient;
import com.ganzi.backend.recommendation.infrastructure.dto.RecommendationSummaryResponse;
import com.ganzi.backend.user.application.UserInterestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationSummaryService {

    private final UserInterestService userInterestService;
    private final AnimalRepository animalRepository;
    private final UpstageChatClient upstageChatClient;
    private final UserInterestPromptFactory promptFactory;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public RecommendationSummaryResponse summarizeUserInterest(Long userId) {
        Map<String, Double> weightMap = userInterestService.buildWeightMap(userId);
        if (weightMap.isEmpty()) {
            return noPreference();
        }

        int topN = 10;
        List<Map.Entry<String, Double>> topEntries = weightMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(topN)
                .toList();

        List<WeightedAnimal> items = new ArrayList<>();

        for (Map.Entry<String, Double> e : topEntries) {
            String desertionNo = e.getKey();
            double weight = e.getValue();

            Animal animal = animalRepository.findByDesertionNo(desertionNo)
                    .orElse(null);
            if (animal == null) {
                continue;
            }

            items.add(new WeightedAnimal(animal, weight));
        }

        if (items.isEmpty()) {
            return noPreference();
        }

        String systemPrompt = promptFactory.buildSystemPrompt();
        String userContent = promptFactory.buildUserContent(items);

        String content;
        try {
            content = upstageChatClient.summarizePreference(systemPrompt, userContent);
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            log.error("UPSTAGE 선호 요약 API 호출 중 예외 발생. userId={}", userId, e);
            throw new GeneralException(ErrorStatus.UPSTAGE_API_CALL_FAILED);
        }

        try {
            return objectMapper.readValue(content, RecommendationSummaryResponse.class);
        } catch (JsonProcessingException e) {
            log.warn("선호 요약 파싱 실패. userId={}, content={}", userId, content, e);
            throw new GeneralException(ErrorStatus.EXTERNAL_API_ERROR);
        }
    }

    public record WeightedAnimal(Animal animal, double weight) {
    }

    public static RecommendationSummaryResponse noPreference() {
        return RecommendationSummaryResponse.builder()
                .data1("아직 유기동물을 충분히 둘러보지 않아 선호를 파악하기 어려워요.")
                .data2("")
                .build();
    }
}