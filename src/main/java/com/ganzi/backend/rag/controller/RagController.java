package com.ganzi.backend.rag.controller;


import com.ganzi.backend.global.code.dto.ApiResponse;
import com.ganzi.backend.global.code.status.SuccessStatus;
import com.ganzi.backend.rag.dto.RagRequest;
import com.ganzi.backend.rag.dto.RagResponse;
import com.ganzi.backend.rag.service.RagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rag")  //기본 엔드포인트 설정
public class RagController {
    private final RagService ragService;

    @GetMapping("/faq")
    public ApiResponse<List<String>> getFaqList() {
        List<String> faqList = ragService.getFaqList();
        return ApiResponse.of(SuccessStatus.OK, faqList);
    }

    //챗봇 질문을 받아 답변을 반환하는 API 엔드 포인트
    @PostMapping("/query")
    public ApiResponse<RagResponse> getChatANswer(
            @RequestBody @Valid RagRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        String username = (userDetails != null)?userDetails.getUsername():"anonymousUser";
        log.info("User ID '{}' 님의 질문입니다.", username);

        RagResponse response = ragService.getAnswer(request);
        return ApiResponse.of(SuccessStatus.OK, response);

    }
}
