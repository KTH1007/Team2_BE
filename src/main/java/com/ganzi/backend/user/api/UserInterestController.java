package com.ganzi.backend.user.api;

import com.ganzi.backend.global.code.dto.ApiResponse;
import com.ganzi.backend.user.api.doc.UserInterestControllerDoc;
import com.ganzi.backend.user.api.dto.RecordInterestRequest;
import com.ganzi.backend.user.application.UserInterestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/user-interests")
public class UserInterestController implements UserInterestControllerDoc {

    private final UserInterestService userInterestService;

    @Override
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> recordUserInterest(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody RecordInterestRequest request
    ) {
        userInterestService.recordInterest(
                userId,
                request.desertionNo(),
                request.dwellTimeSeconds()
        );
        return ResponseEntity.ok(ApiResponse.onSuccess("사용자 행동 로그를 저장했습니다."));
    }

    @Override
    @PostMapping("/{userId}/embedding")
    public ResponseEntity<ApiResponse<String>> recomputeUserEmbedding(
            @PathVariable("userId") Long userId
    ) {
        userInterestService.computeUserEmbedding(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess("사용자 관심 임베딩을 갱신했습니다."));
    }
}
