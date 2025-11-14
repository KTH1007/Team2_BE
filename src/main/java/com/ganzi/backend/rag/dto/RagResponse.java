package com.ganzi.backend.rag.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RagResponse {

    // LLM이 생성한 최종 답변
    private String answer;

   //RAG 사용 x (의미 없음)
   //private List<SourceDocument> sources;
}
