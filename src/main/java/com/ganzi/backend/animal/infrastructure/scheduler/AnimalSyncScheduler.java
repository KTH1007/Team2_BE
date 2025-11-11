package com.ganzi.backend.animal.infrastructure.scheduler;

import com.ganzi.backend.animal.application.AnimalSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnimalSyncScheduler {

    private final AnimalSyncService animalSyncService;

    @Scheduled(cron = "0 0 3 * * *")
    public void syncDailyAbandonedAnimals() {
        log.info("일일 유기동물 스케줄 동기화 시작");
        try {
            animalSyncService.dailySync();
        } catch (Exception e) {
            log.error("일일 유기동물 스케줄 동기화 실패");
        }
    }
}
