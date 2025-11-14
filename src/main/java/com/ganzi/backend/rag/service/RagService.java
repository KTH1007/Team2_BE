package com.ganzi.backend.rag.service;

import com.ganzi.backend.rag.dto.*;
import com.ganzi.backend.global.exception.GeneralException;
import com.ganzi.backend.global.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor    // webclient 등을 생성자 주입으로 받기 위해
@Slf4j
public class RagService {

    // WebClient는 RagConfig에서 Bean으로 등록한 것을 주입받는다.
    private final WebClient ragApiClient;

    // API Key와 URL 은 application.yml 등 설정 파일에서 읽어온다.
    @Value("${upstage.ai-space.api-key}")
    private String apiKey;

    @Value("${upstage.ai-space.endpoint-url}")
    private String endpointUrl;

    //@Value("${upstage.ai-space.rag-id}")
    //private String ragId;

    public List<String> getFaqList() {
        return List.of(
                "유기동물 입양 절차 알려주세요!",
                "한국에서 가장 인기많은 반려동물은 뭔가요?",
                "입양 전 보호소 방문이 가능한가요?"

        );
    }

    public RagResponse getAnswer(RagRequest request){
        log.info("RAG Service 진입 - 쿼리: {}", request.getQuery());
        return callUpstageApi(request.getQuery());
    }

    // API 호출 로직을 분리한 프라이빗 메서드
    private RagResponse callUpstageApi(String query){
        //log.debug("Upstage Endpoint URL: {}", endpointUrl);
        //log.debug("RAG ID: {}", ragId);

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", query
        );
        Map<String, Object> requestBody = Map.of(
                "model", "solar-pro2",
                //"rag_id", List.of(ragId),
                "messages", List.of(message),
                "stream", false
        );
        //log.debug("Request Body: {}", requestBody);

        UpstageApiResponse apiResponse = ragApiClient.post()
                .uri(endpointUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status->status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> {
                            // API 호출 실패 시 HTTP 상태 코드와 응답 본문을 로그에 기록합니다.
                            log.error("Upstage API 호출 실패! HTTP Status: {}", clientResponse.statusCode());

                            // 응답 본문을 String으로 읽어 추가 로그 기록 (오류 진단 핵심)
                            return clientResponse.bodyToMono(String.class)
                                    .flatMap(body -> {
                                        log.error("Upstage API 응답 본문: {}", body);
                                        return Mono.error(new GeneralException(ErrorStatus.RAG_API_CALL_FAILED));
                                    });
                        })
                .bodyToMono(UpstageApiResponse.class)//응답을 임시 DTO로 변환
                .block();   //비동기 코드를 동시적으로 기다림

        String answer = Optional.ofNullable(apiResponse)
                .flatMap(r -> r.getChoices().stream().findFirst())
                .map(c -> c.getMessage())
                .map(m -> m.getContent())
                .orElse(null); // 답변이 없을 경우 null 할당

        if (answer == null) {
            log.error("LLM 응답 결과에서 content 필드를 추출할 수 없습니다. 응답 구조를 다시 확인하세요.");
            throw new GeneralException(ErrorStatus.RAG_API_INTERNAL_FAILURE);
        }

        //List<SourceDocument> sources = List.of();

        //6. Upstage DTO를 프로젝트의 RagResponse로 변환하여 반환
        return RagResponse.builder()
                .answer(answer)
                //.sources(sources)
                .build();
    }
}
