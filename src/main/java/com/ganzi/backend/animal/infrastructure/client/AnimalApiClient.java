package com.ganzi.backend.animal.infrastructure.client;

import com.ganzi.backend.animal.infrastructure.dto.AnimalApiResponse;
import com.ganzi.backend.global.code.status.ErrorStatus;
import com.ganzi.backend.global.exception.GeneralException;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnimalApiClient {

    private static final String ABANDON_API_PATH = "/abandonmentPublic_v2";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int PAGE_SIZE = 1000;

    @Value("${animal.api.base-url}")
    private String baseUrl;

    @Value("${animal.api.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    public AnimalApiResponse fetchAbandonedAnimals(LocalDate startDate, LocalDate endDate, int pageNo) {
        String url = buildUrl(startDate, endDate, pageNo);
        try {
            AnimalApiResponse response = restTemplate.getForObject(url, AnimalApiResponse.class);
            return response;
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                log.error("공공 API 타임아웃: pageNo={}", pageNo, e);
                throw new GeneralException(ErrorStatus.ANIMAL_API_TIMEOUT);
            }
            log.error("공공 API 연결 실패: pageNo={}", pageNo, e);
            throw new GeneralException(ErrorStatus.ANIMAL_API_CALL_FAILED);
        } catch (RestClientException e) {
            log.error("공공 API 연결 실패: pageNo={}", pageNo, e);
            throw new GeneralException(ErrorStatus.ANIMAL_API_CALL_FAILED);
        }
    }

    private String buildUrl(LocalDate startDate, LocalDate endDate, int pageNo) {
        return UriComponentsBuilder.fromUriString(baseUrl + ABANDON_API_PATH)
                .queryParam("serviceKey", serviceKey)
                .queryParam("bgnde", startDate.format(DATE_TIME_FORMATTER))
                .queryParam("endde", endDate.format(DATE_TIME_FORMATTER))
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", PAGE_SIZE)
                .queryParam("_type", "json")
                .encode()
                .toUriString();
    }
}
