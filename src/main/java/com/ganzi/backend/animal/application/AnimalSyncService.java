package com.ganzi.backend.animal.application;

import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.repository.AnimalRepository;
import com.ganzi.backend.animal.infrastructure.client.AnimalApiClient;
import com.ganzi.backend.animal.infrastructure.dto.AnimalApiItem;
import com.ganzi.backend.animal.infrastructure.dto.AnimalApiResponse;
import com.ganzi.backend.animal.infrastructure.mapper.AnimalMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalSyncService {

    private static final int INITIAL_SYNC_DAYS = 7;
    private static final int DAILY_SYNC_DAYS = 1;
    private static final int INITIAL_PAGE_NO = 1;
    private static final int PAGE_SIZE = 1000;

    private final AnimalApiClient animalApiClient;
    private final AnimalMapper animalMapper;
    private final AnimalRepository animalRepository;

    @Transactional
    public void syncAbandonedAnimals() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(INITIAL_SYNC_DAYS);
        syncAllPages(startDate, endDate);
    }

    @Transactional
    public void dailySync() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(DAILY_SYNC_DAYS);
        syncAllPages(startDate, endDate);
    }

    private int syncAllPages(LocalDate startDate, LocalDate endDate) {
        int pageNo = INITIAL_PAGE_NO;
        int totalSaved = 0;

        while (true) {
            AnimalApiResponse response = animalApiClient.fetchAbandonedAnimals(startDate, endDate, pageNo);

            List<AnimalApiItem> items = extractItems(response);
            if (items == null || items.isEmpty()) {
                break;
            }

            int savedCount = saveAnimalsInTransaction(items);
            totalSaved += savedCount;

            if (isLastPage(items)) {
                break;
            }
            pageNo++;
        }

        return totalSaved;
    }

    @Transactional
    public int saveAnimalsInTransaction(List<AnimalApiItem> items) {
        List<Animal> animalsToSave = new ArrayList<>();

        for (AnimalApiItem item : items) {
            String desertionNo = item.getDesertionNo();
            if (animalRepository.existsById(desertionNo)) {
                continue;
            }

            try {
                Animal animal = animalMapper.toEntity(item);
                animalsToSave.add(animal);
            } catch (Exception e) {
                log.error("유기동물 변환 실패: desertionNo={}, error={}", desertionNo, e.getMessage());
            }
        }

        if (!animalsToSave.isEmpty()) {
            animalRepository.saveAll(animalsToSave);
        }

        return animalsToSave.size();
    }

    private List<AnimalApiItem> extractItems(AnimalApiResponse response) {
        if (response == null) {
            log.warn("API 응답이 null입니다.");
            return null;
        }

        AnimalApiResponse.Response responseData = response.getResponse();
        if (responseData == null) {
            log.warn("Response 데이터가 null입니다.");
            return null;
        }

        AnimalApiResponse.Body body = responseData.getBody();
        if (body == null) {
            log.info("Body 데이터가 없습니다.");
            return null;
        }

        AnimalApiResponse.Items itemsWrapper = body.getItems();
        if (itemsWrapper == null) {
            log.info("Items 데이터가 없습니다.");
            return null;
        }

        return itemsWrapper.getItem();
    }

    private boolean isLastPage(List<AnimalApiItem> items) {
        int itemSize = items.size();
        return itemSize < PAGE_SIZE;
    }
}
