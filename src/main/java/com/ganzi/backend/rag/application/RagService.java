package com.ganzi.backend.rag.application;

import com.ganzi.backend.rag.api.dto.request.RagRequest;
import com.ganzi.backend.rag.api.dto.response.RagResponse;
import com.ganzi.backend.rag.api.dto.response.UpstageApiResponse;
import com.ganzi.backend.global.exception.GeneralException;
import com.ganzi.backend.global.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
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
        log.info("RAG Service 진입 - 쿼리: {}", request.query());
        Map<String, Object> requestBody = buildApiRequest(request.query());
        UpstageApiResponse apiResponse = fetchApiResponse(requestBody);
        String answer = extractAnswerFromResponse(apiResponse);
        // List<SourceDocument> sources = List.of();
        return new RagResponse(answer);
    }

    private Map<String, Object> buildApiRequest(String query) {
        Map<String, Object> message = Map.of(
                "role", "user",
                "content", query
        );

        return Map.of(
                "model", "solar-pro2",
                "messages", List.of(message),
                "stream", false
                //"rag_ids", List.of(ragId)
        );
    }
    // API 호출 로직을 분리
    private UpstageApiResponse fetchApiResponse(Map<String,Object> requestBody) {

        return ragApiClient.post()
                .uri(endpointUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        this::handleApiError)
                .bodyToMono(UpstageApiResponse.class)//응답을 임시 DTO로 변환
                .block();   //비동기 코드를 동시적으로 기다림
    }
    private Mono<Throwable> handleApiError(ClientResponse clientResponse){
        log.error("Upstage API 호출 실패! HTTP Status: {}", clientResponse.statusCode());
        return clientResponse.bodyToMono(String.class)
                .flatMap(body -> {
                    log.error("Upstage API 응답 본문: {}", body);
                    return Mono.error(new GeneralException(ErrorStatus.RAG_API_CALL_FAILED));
                });

    }

    private String extractAnswerFromResponse(UpstageApiResponse apiResponse) {
        String answer = Optional.ofNullable(apiResponse)
                .flatMap(r -> r.choices().stream().findFirst())
                .map(c -> c.message())
                .map(m -> m.content())
                .orElse(null); // 답변이 없을 경우 null 할당

        if (answer == null) {
            log.error("LLM 응답 결과에서 content 필드를 추출할 수 없습니다. 응답 구조를 다시 확인하세요.");
            throw new GeneralException(ErrorStatus.RAG_API_INTERNAL_FAILURE);
        }

        return answer;
    }
}
