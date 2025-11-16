package com.ganzi.backend.rag.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "챗봇 질문 요청")
public record RagRequest (


    @Schema(description = "사용자 질문 내용",example = "한국인이 많이 키우는 반려동물 1위는?")
    @NotBlank(message = "무엇이든 물어보세요.")
    String query

    // 대화의 연속성을 위한 세션 ID 또는 사용자ID 추가 고려
    // String userId;
){}
