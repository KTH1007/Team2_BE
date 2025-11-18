package com.ganzi.backend.user.api;

import com.ganzi.backend.global.code.dto.ApiResponse;
import com.ganzi.backend.user.api.doc.UserLikeControllerDoc;
import com.ganzi.backend.user.api.dto.RecordLikeRequest;
import com.ganzi.backend.user.application.UserLikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user-likes")
@RequiredArgsConstructor
public class UserLikeController implements UserLikeControllerDoc {

    private final UserLikeService userLikeService;

    @Override
    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> recordUserLike(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody RecordLikeRequest request
    ) {
        userLikeService.setUserLike(userId, request.desertionNo(), request.liked());
        return ResponseEntity.ok(ApiResponse.onSuccess("찜 상태가 저장되었습니다."));
    }

    @Override
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<String>>> getUserLikes(
            @PathVariable("userId") Long userId
    ) {
        List<String> likedList = userLikeService.getLikedDesertionNos(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess(likedList));
    }
}
