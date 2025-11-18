package com.ganzi.backend.user.api.doc;

import com.ganzi.backend.global.code.dto.ApiResponse;
import com.ganzi.backend.user.api.dto.RecordLikeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(
        name = "사용자 북마크 관리",
        description = "사용자의 북마크 상태를 기록하고 조회하는 API"
)
public interface UserLikeControllerDoc {

    @Operation(
            summary = "사용자 북마크 상태 기록",
            description = """
                    특정 사용자가 동물에 대해 북마크를 하거나 해제하는 요청을 기록합니다.

                    ### 엔드포인트
                    - POST /api/admin/user-likes/{userId}

                    ### Path Variable
                    - userId: 북마크 상태를 변경할 사용자 ID (PK)

                    ### Request Body
                    - desertionNo: 유기동물 식별자(공공 API 구조번호)
                    - liked: true 이면 북마크 등록, false 이면 북마크 취소

                    ### 동작 설명
                    - UserLike 테이블에 사용자-동물 조합이 존재하면 liked 상태를 업데이트하고,
                      존재하지 않으면 새로 생성합니다.
                    """
    )
    ResponseEntity<ApiResponse<String>> recordUserLike(Long userId, RecordLikeRequest request);

    @Operation(
            summary = "사용자 북마크 목록 조회",
            description = """
                    사용자가 현재 북마크에 등록한 유기동물의 구조번호 목록을 조회합니다.

                    ### 엔드포인트
                    - GET /api/admin/user-likes/{userId}

                    ### Path Variable
                    - userId: 북마크 목록을 조회할 사용자 ID (PK)
                    """
    )
    ResponseEntity<ApiResponse<List<String>>> getUserLikes(Long userId);
}
