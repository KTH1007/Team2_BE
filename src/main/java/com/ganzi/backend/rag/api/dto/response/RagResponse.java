package com.ganzi.backend.rag.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "챗봇 답변 응답")
public record RagResponse (

    @Schema(description = "LLM이 생성한 답변")
    String answer

   //RAG 사용 x (의미 없음)
   //List<SourceDocument> sources;
){}
