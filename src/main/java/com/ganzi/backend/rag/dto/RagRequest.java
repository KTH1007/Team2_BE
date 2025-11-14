package com.ganzi.backend.rag.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RagRequest {

    @NotBlank(message = "무엇이든 물어보세요.")
    private String query;

    // 대화의 연속성을 위한 세션 ID 또는 사용자ID 추가 고려
    // private String userId;

}
