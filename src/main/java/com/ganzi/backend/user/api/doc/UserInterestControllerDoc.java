package com.ganzi.backend.user.api.doc;

import com.ganzi.backend.global.code.dto.ApiResponse;
import com.ganzi.backend.user.api.dto.RecordInterestRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "사용자 행동 기록",
        description = "사용자의 유기동물 상세 페이지 방문/체류/좋아요 기록을 저장하고, 관심 임베딩을 갱신하는 API"
)
public interface UserInterestControllerDoc {

    @Operation(
            summary = "사용자 행동 데이터 기록",
            description = """
                    특정 사용자가 유기동물 상세 페이지에 방문했을 때의 행동 데이터를 기록합니다.
                    
                    기록된 모든 상호작용(방문, 체류 시간, 좋아요 여부)을 기반으로
                    사용자의 관심 임베딩(UserEmbedding)을 재계산하여 업데이트합니다.
                    
                    ### 엔드포인트
                    - POST /api/user-interests/{userId}/interests
                    
                    ### Path Variable
                    - userId: 행동을 기록할 사용자 ID (PK)
                    
                    ### Request Body
                    - desertionNo: 유기동물 식별자(공공 API 구조번호)
                    - dwellTimeSeconds: 페이지 체류 시간 (초 단위, 선택 값 / null이면 0으로 처리)
                    - liked: 해당 동물에 대한 좋아요 여부 (true/false)
                    
                    ### 동작 설명
                    - UserInterest 테이블에 한 번의 상호작용(visit, dwell, like)을 저장
                    - 저장된 사용자-동물 상호작용 전체를 조회
                    - 각 동물의 임베딩(AnimalEmbedding)을 가져와 가중 평균 벡터 계산
                        - liked = true 인 경우 높은 가중치(1.0)
                        - liked = false 인 경우
                          - 기본 가중치 0.1
                          - 체류 시간 1초당 0.02씩 추가
                          - 최대 0.3 까지 가중치 상한
                    - 계산된 벡터를 L2 정규화 후 UserEmbedding에 저장 또는 갱신
                    """
    )
    ResponseEntity<ApiResponse<String>> recordUserInterest(Long userId, RecordInterestRequest request);
}
