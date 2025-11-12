package com.ganzi.backend.animal.api;

import com.ganzi.backend.animal.api.doc.AnimalAdminControllerDoc;
import com.ganzi.backend.animal.application.AnimalSyncService;
import com.ganzi.backend.global.code.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/animals")
public class AnimalAdminController implements AnimalAdminControllerDoc {

    private final AnimalSyncService animalSyncService;

    @Override
    @PostMapping("/sync/initial")
    public ResponseEntity<ApiResponse<String>> syncInitial() {
        animalSyncService.syncAbandonedAnimals();
        return ResponseEntity.ok(ApiResponse.onSuccess("초기 동기화가 완료되었습니다."));
    }

    @Override
    @PostMapping("/sync/daily")
    public ResponseEntity<ApiResponse<String>> syncDaily() {
        animalSyncService.dailySync();
        return ResponseEntity.ok(ApiResponse.onSuccess("일일 동기화가 완료되었습니다."));
    }
}
