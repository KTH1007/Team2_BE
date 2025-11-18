package com.ganzi.backend.user.api.doc;

import com.ganzi.backend.global.code.dto.ApiResponse;
import com.ganzi.backend.user.api.dto.RecordInterestRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "사용자 행동 기록",
        description = "사용자의 유기동물 상세 페이지 방문/체류 로그를 저장하고, AI 추천용 임베딩을 갱신하는 API"
)
public interface UserInterestControllerDoc {

    @Operation(
            summary = "사용자 행동 데이터 기록",
            description = """
                    특정 사용자가 유기동물 상세 페이지에 방문했을 때의 행동 데이터를 기록합니다.
                    
                    ### 엔드포인트
                    - POST /api/admin/user-interests/{userId}
                    
                    ### Path Variable
                    - userId: 행동을 기록할 사용자 ID (PK)
                    
                    ### Request Body
                    - desertionNo: 유기동물 식별자(공공 API 구조번호)
                    - dwellTimeSeconds: 페이지 체류 시간 (초 단위, 선택 값 / null이면 0으로 처리)
                    
                    ### 동작 설명
                    - UserInterest 테이블에 방문/체류 로그를 저장합니다.
                    - 북마크 여부는 UserLike 테이블에서 별도로 관리합니다.
                    - 임베딩 갱신은 별도 엔드포인트에서 수행합니다.
                    """
    )
    ResponseEntity<ApiResponse<String>> recordUserInterest(Long userId, RecordInterestRequest request);

    @Operation(
            summary = "사용자 관심 임베딩 재계산",
            description = """
                    사용자의 행동 로그(UserInterest)와 북마크 정보(UserLike)를 바탕으로
                    AI 추천에 사용할 관심 임베딩(UserEmbedding)을 재계산합니다.
                    
                    ### 엔드포인트
                    - POST /api/admin/user-interests/{userId}/embedding
                    
                    ### Path Variable
                    - userId: 임베딩을 재계산할 사용자 ID (PK)
                    
                    ### 동작 설명
                    - UserInterest의 dwellTime 기반 가중치와
                      UserLike의 liked=true인 동물에 대한 가중치를 합산하여 벡터를 계산합니다.
                    """
    )
    ResponseEntity<ApiResponse<String>> recomputeUserEmbedding(Long userId);
}
