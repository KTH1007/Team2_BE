package com.ganzi.backend.animal.api;

import com.ganzi.backend.animal.api.doc.AnimalEmbeddingAdminControllerDoc;
import com.ganzi.backend.animal.application.AnimalEmbeddingService;
import com.ganzi.backend.global.code.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/animals/embedding")
public class AnimalEmbeddingAdminController implements AnimalEmbeddingAdminControllerDoc {

    private final AnimalEmbeddingService embeddingService;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<String>> generateAll() {
        embeddingService.generateAllEmbeddingsAsync();

        return ResponseEntity.accepted()
                .body(ApiResponse.onSuccess(null));
    }
}
