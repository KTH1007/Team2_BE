package com.ganzi.backend.recommendation.infrastructure.client;

import com.ganzi.backend.recommendation.infrastructure.dto.UpstageChatRequest;
import com.ganzi.backend.recommendation.infrastructure.dto.UpstageChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpstageChatClient {

    private final RestTemplate restTemplate;

    @Value("${upstage.chat.url:https://api.upstage.ai/v1/solar/chat/completions}")
    private String chatUrl;

    @Value("${upstage.api.key}")
    private String apiKey;

    @Value("${upstage.chat.model:solar-1-mini-chat}")
    private String model;

    public String summarizePreference(String systemPrompt, String userContent) {
        UpstageChatRequest requestBody = buildRequest(systemPrompt, userContent);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<UpstageChatRequest> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<UpstageChatResponse> response = restTemplate.exchange(
                    chatUrl,
                    HttpMethod.POST,
                    entity,
                    UpstageChatResponse.class
            );

            UpstageChatResponse body = response.getBody();
            if (body == null || body.getChoices() == null || body.getChoices().isEmpty()) {
                log.warn("Upstage Chat 응답이 비어있음");
                return "선호 요약 문장을 생성하지 못했어요.";
            }

            UpstageChatResponse.Choice first = body.getChoices().getFirst();
            if (first.getMessage() == null || first.getMessage().getContent() == null) {
                log.warn("Upstage Chat 응답에 message.content 없음");
                return "선호 요약 문장을 생성하지 못했어요.";
            }

            return first.getMessage().getContent().trim();
        } catch (Exception e) {
            log.error("Upstage Chat 호출 실패", e);
            return "선호 요약 문장을 생성하는 중 오류가 발생했어요.";
        }
    }

    private UpstageChatRequest buildRequest(String systemPrompt, String userContent) {
        UpstageChatRequest req = new UpstageChatRequest();
        req.setModel(model);

        UpstageChatRequest.Message systemMsg = new UpstageChatRequest.Message();
        systemMsg.setRole("system");
        systemMsg.setContent(systemPrompt);

        UpstageChatRequest.Message userMsg = new UpstageChatRequest.Message();
        userMsg.setRole("user");
        userMsg.setContent(userContent);

        req.setMessages(List.of(systemMsg, userMsg));
        return req;
    }
}
