package com.ganzi.backend.animal.infrastructure.client;

import com.ganzi.backend.animal.infrastructure.dto.AnimalApiResponse;
import com.ganzi.backend.global.code.status.ErrorStatus;
import com.ganzi.backend.global.exception.GeneralException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnimalApiClient {

    private static final String ABANDON_API_PATH = "/abandonmentPublic_v2";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int PAGE_SIZE = 1000;
    private static final String URL_TEMPLATE = "%s%s?serviceKey=%s&bgnde=%s&endde=%s&pageNo=%d&numOfRows=%d&_type=json";

    @Value("${animal.api.base-url}")
    private String baseUrl;

    @Value("${animal.api.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    public AnimalApiResponse fetchAbandonedAnimals(LocalDate startDate, LocalDate endDate, int pageNo) {
        String url = buildUrl(startDate, endDate, pageNo);

        try {
            URI uri = URI.create(url);
            return restTemplate.getForObject(uri, AnimalApiResponse.class);
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
        String url = String.format(
                URL_TEMPLATE,
                baseUrl,
                ABANDON_API_PATH,
                serviceKey,
                startDate.format(DATE_TIME_FORMATTER),
                endDate.format(DATE_TIME_FORMATTER),
                pageNo,
                PAGE_SIZE
        );

        return url;
    }
}
