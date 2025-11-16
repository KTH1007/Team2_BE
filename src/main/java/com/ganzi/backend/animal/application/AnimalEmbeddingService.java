package com.ganzi.backend.animal.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.AnimalEmbedding;
import com.ganzi.backend.animal.domain.repository.AnimalEmbeddingRepository;
import com.ganzi.backend.animal.domain.repository.AnimalRepository;
import com.ganzi.backend.animal.infrastructure.client.UpstageEmbeddingClient;
import com.ganzi.backend.animal.infrastructure.mapper.AnimalEmbeddingMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalEmbeddingService {

    private final AnimalRepository animalRepository;
    private final AnimalEmbeddingRepository embeddingRepository;
    private final UpstageEmbeddingClient embeddingClient;
    private final AnimalEmbeddingMapper embeddingMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    public void generateAllEmbeddings() {
        List<Animal> animals = animalRepository.findAll();
        log.info("{} 개의 동물 임베딩을 생성 중입니다", animals.size());
        for (Animal animal : animals) {
            generateEmbedding(animal);
        }
    }

    @Transactional
    public void generateEmbedding(Animal animal) {
        String input = embeddingMapper.toEmbeddingInput(animal);
        if (input.isBlank()) {
            return;
        }
        List<float[]> vectors = embeddingClient.embedTexts(List.of(input));
        if (vectors.isEmpty()) {
            log.warn("DesertionNo : {} , 해당 유기동물 임베딩에 실패했습니다", animal.getDesertionNo());
            return;
        }
        float[] vector = vectors.getFirst();
        try {
            String json = objectMapper.writeValueAsString(vector);
            AnimalEmbedding embedding = AnimalEmbedding.builder()
                    .animal(animal)
                    .embeddingJson(json)
                    .dimension(vector.length)
                    .build();
            embeddingRepository.save(embedding);
        } catch (JsonProcessingException e) {
            log.error("DesertionNo : {}, 직렬화에 실패했습니다", animal.getDesertionNo(), e);
        }
    }
}
