package com.ganzi.backend.recommendation.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ganzi.backend.animal.domain.AnimalEmbedding;
import com.ganzi.backend.animal.domain.repository.AnimalEmbeddingRepository;
import com.ganzi.backend.user.domain.UserEmbedding;
import com.ganzi.backend.user.domain.repository.UserEmbeddingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final UserEmbeddingRepository userEmbeddingRepository;
    private final AnimalEmbeddingRepository animalEmbeddingRepository;
    private final ObjectMapper objectMapper;

    public List<String> recommend(Long userId, int top) {
        Optional<UserEmbedding> optUserEmbedding = userEmbeddingRepository.findByUserId(userId);
        float[] userVector = null;
        if (optUserEmbedding.isPresent()) {
            try {
                userVector = objectMapper.readValue(
                        optUserEmbedding.get().getEmbeddingJson(), new TypeReference<float[]>() {});
            } catch (JsonProcessingException e) {
                log.warn("user {} 임베딩 오류 : 역직렬화 실패", userId, e);
            }
        }

        List<AnimalEmbedding> embeddings = animalEmbeddingRepository.findAll();

        if (userVector == null || userVector.length == 0) {
            return embeddings.stream()
                    .map(AnimalEmbedding::getDesertionNo)
                    .limit(top)
                    .toList();
        }

        List<RecommendationScore> scores = calculateScores(userVector, embeddings);
        scores.sort(Comparator.comparingDouble(RecommendationScore::score).reversed());

        return scores.stream()
                .limit(top)
                .map(RecommendationScore::desertionNo)
                .toList();
    }

    private List<RecommendationScore> calculateScores(float[] userVector, List<AnimalEmbedding> embeddings) {
        List<RecommendationScore> scores = new ArrayList<>();

        for (AnimalEmbedding emb : embeddings) {
            try {
                float[] animalVector = objectMapper.readValue(
                        emb.getEmbeddingJson(), new TypeReference<float[]>() {});
                if (animalVector.length != userVector.length) {
                    continue;
                }
                double score = cosineSimilarity(userVector, animalVector);
                scores.add(new RecommendationScore(emb.getDesertionNo(), score));
            } catch (JsonProcessingException e) {
                log.warn("animal {} 임베딩 오류 : Score 계산 실패", emb.getDesertionNo(), e);
            }
        }

        return scores;
    }


    private double cosineSimilarity(float[] a, float[] b) {
        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0.0 || normB == 0.0) {
            return 0.0;
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
    private record RecommendationScore(String desertionNo, double score) {}
}
