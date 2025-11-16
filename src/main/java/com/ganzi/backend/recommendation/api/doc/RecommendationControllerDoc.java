package com.ganzi.backend.recommendation.api.doc;

import com.ganzi.backend.global.code.dto.ApiResponse;
import com.ganzi.backend.animal.api.dto.response.AnimalDetailResponse;
import com.ganzi.backend.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(
        name = "추천 API",
        description = "사용자 관심 임베딩을 기반으로 유기동물을 추천하는 API"
)
public interface RecommendationControllerDoc {

    @Operation(
            summary = "사용자 맞춤 유기동물 추천",
            description = """
                    
                    주어진 userId에 대한 관심 임베딩과 각 동물의 임베딩 간 코사인 유사도를 계산하여 \
                    유사도가 높은 순으로 N마리(기본 8마리)의 유기동물을 추천합니다.
                    유저 임베딩이 없을 경우에는 등록된 동물 중 임베딩이 있는 순서대로 기본 추천을 제공합니다.
                    
                    ### 요청 파라미터
                    - **userId**: 추천을 요청하는 사용자의 식별자
                    - **top**: 반환할 추천 동물의 최대 개수 (선택, 기본값 8)
                    
                    ### 응답
                    `ApiResponse<List<AnimalDetailResponse>>` 형태로 추천된 동물들의 상세 정보를 리스트로 반환합니다.
                    """
    )

    ResponseEntity<ApiResponse<List<AnimalDetailResponse>>> recommendAnimals(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "top", defaultValue = "8") Integer top
    );
}