package com.ganzi.backend.rag.controller;

import com.ganzi.backend.global.code.dto.ApiResponse;
import com.ganzi.backend.global.code.status.SuccessStatus;
import com.ganzi.backend.global.security.userdetails.CustomUserDetails;
import com.ganzi.backend.rag.dto.RagRequest;
import com.ganzi.backend.rag.dto.RagResponse;
import com.ganzi.backend.rag.service.RagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RagController implements RagControllerDoc{
    private final RagService ragService;

    @Override
    public ResponseEntity<ApiResponse<List<String>>> getFaqList() {
        List<String> faqList = ragService.getFaqList();
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus.OK, faqList));
    }

    //챗봇 질문을 받아 답변을 반환하는 API 엔드 포인트
    @Override
    public ResponseEntity<ApiResponse<RagResponse>> getChatAnswer(
            @RequestBody @Valid RagRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        //String username = (customUserDetails != null)?customUserDetails.getUsername():"anonymousUser";
        String userId = customUserDetails.getUsername();
        log.info("User ID '{}' 님의 질문입니다.", userId);
        //log.info("User ID '{}' 님의 질문입니다.", username);

        RagResponse response = ragService.getAnswer(request);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus.OK, response));

    }
}
