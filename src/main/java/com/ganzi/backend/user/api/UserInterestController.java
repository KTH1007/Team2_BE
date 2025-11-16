package com.ganzi.backend.user.api;

import com.ganzi.backend.global.code.dto.ApiResponse;
import com.ganzi.backend.user.application.UserInterestService;
import com.ganzi.backend.user.api.doc.UserInterestControllerDoc;
import com.ganzi.backend.user.api.dto.RecordInterestRequest;
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
    @PostMapping("/{userId}/interests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> recordUserInterest(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody RecordInterestRequest request) {

        userInterestService.recordInterest(
                userId,
                request.desertionNo(),
                request.dwellTimeSeconds(),
                request.liked()
        );

        return ResponseEntity.ok(ApiResponse.onSuccess("유저의 행동 데이터를 기록하였습니다"));
    }
}
