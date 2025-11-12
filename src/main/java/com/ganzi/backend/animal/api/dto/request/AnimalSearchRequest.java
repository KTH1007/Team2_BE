package com.ganzi.backend.animal.api.dto.request;

import com.ganzi.backend.animal.domain.AnimalType;
import com.ganzi.backend.animal.domain.NeuterStatus;
import com.ganzi.backend.animal.domain.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@Schema(description = "유기동물 검색 조건")
public record AnimalSearchRequest(

        @Schema(description = "검색 시작일 (yyyyMMdd)", example = "20250809")
        @Pattern(regexp = "^\\d{8}$", message = "날짜 형식은 yyyyMMdd 형식이어야 합니다")
        String startDate,

        @Schema(description = "검색 종료일 (yyyyMMdd)", example = "20251109")
        @Pattern(regexp = "^\\d{8}$", message = "날짜 형식은 yyyyMMdd 형식이어야 합니다")
        String endDate,

        @Schema(description = "시/도", example = "서울특별시")
        String province,

        @Schema(description = "시/군/구", example = "강남구")
        String city,

        @Schema(description = "축종", example = "DOG")
        AnimalType animalType,

        @Schema(description = "성별", example = "MALE")
        Sex sex,

        @Schema(description = "중성화 여부", example = "YES")
        NeuterStatus neuterStatus,

        @Schema(description = "보호중인 동물만 검색", example = "true")
        Boolean onlyProtecting,

        @Schema(description = "최신순 정렬 (true: 최신순, false: 오래된순)", example = "true")
        Boolean isLatest
) {
}
