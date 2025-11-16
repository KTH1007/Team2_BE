package com.ganzi.backend.rag.api.dto.response;

// 응답에 포함될 출처 정보
// RAG 사용 불가한 현시점에서는 의미 x
public record SourceDocument(
    String documentId,  // 검색된 문서 고유 ID
    String snippet,     // 답변에 활용된 문서 일부 내용
    String link           // (선택) 문서 원본 링크
){}
