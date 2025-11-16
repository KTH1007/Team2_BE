package com.ganzi.backend.rag.api.doc;

import com.ganzi.backend.global.code.dto.ApiResponse;
import com.ganzi.backend.global.security.userdetails.CustomUserDetails;
import com.ganzi.backend.rag.api.dto.request.RagRequest;
import com.ganzi.backend.rag.api.dto.response.RagResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "챗봇 API", description = "챗봇 기능 관련 API입니다.")
@RequestMapping("/api/rag")
public interface RagControllerDoc {
    @Operation(
            summary = "자주 묻는 질문(FAQ) 목록 조회",
            description = "챗봇 UI의 '자주 묻는 질문' 버튼 목록을 조회합니다.(질문내용 고정됨)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK: 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "UNAUTHORIZED: 인증 실패", content = @Content)
    })
    @GetMapping("/faq")
    ResponseEntity<ApiResponse<List<String>>> getFaqList();


    @Operation(
            summary = "챗봇 답변 요청",
            description = "사용자의 query를 받아 LLM의 답변(answer)을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK: 요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "BAD_REQUEST: 쿼리 누락", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "UNAUTHORIZED: 인증 실패", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR: 서버 내부 오류 (RAG500 또는 RAG501)",
                    content = @Content
            )
    })
    @PostMapping("/query")
    ResponseEntity<ApiResponse<RagResponse>> getChatAnswer(
            @RequestBody @Valid RagRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    );
}
