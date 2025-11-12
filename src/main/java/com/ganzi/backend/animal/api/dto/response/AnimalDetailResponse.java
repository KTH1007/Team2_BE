package com.ganzi.backend.animal.api.dto.response;

import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.AnimalImage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "유가동물 상세 조회")
@Builder
public record AnimalDetailResponse(
        @Schema(description = "구조번호")
        String desertionNo,

        @Schema(description = "동물등록번호")
        String rfidCode,

        @Schema(description = "이미지")
        List<String> images,

        @Schema(description = "품종명")
        String breedName,

        @Schema(description = "축종명")
        String animalTypeName,

        @Schema(description = "접수일")
        String foundDate,

        @Schema(description = "성별")
        String sex,

        @Schema(description = "중성화 여부")
        String neuterStatus,

        @Schema(description = "나이")
        String age,

        @Schema(description = "체중")
        String weight,

        @Schema(description = "보호 상태")
        String status,

        @Schema(description = "시/도")
        String province,

        @Schema(description = "시/군/구")
        String city,

        @Schema(description = "공고시작일")
        String noticeStartDate,

        @Schema(description = "공고종료일")
        String noticeEndDate,

        @Schema(description = "보호소 이름")
        String shelterName,

        @Schema(description = "보호소 전화번호")
        String shelterTel,

        @Schema(description = "보호소 장소")
        String shelterAddress,

        @Schema(description = "특징")
        String specialMark,

        @Schema(description = "발견 장소")
        String foundPlace,

        @Schema(description = "색상")
        String color,

        @Schema(description = "특징-건강 정보")
        String healthInfo,

        @Schema(description = "백신 접종 정보")
        String vaccination,

        @Schema(description = "건강 정보")
        String healthCheck,

        @Schema(description = "특징-사회성")
        String personality
) {
    public static AnimalDetailResponse from(Animal animal) {
        List<String> imageUrls = animal.getImages().stream()
                .map(AnimalImage::getImageUrl)
                .toList();

        return AnimalDetailResponse.builder()
                .desertionNo(animal.getDesertionNo())
                .rfidCode(animal.getRfidCode())
                .images(imageUrls)
                .breedName(animal.getBreedName())
                .animalTypeName(animal.getAnimalType().getDescription())
                .foundDate(animal.getFoundDate())
                .age(animal.getAge())
                .sex(animal.getSex().getDescription())
                .neuterStatus(animal.getNeuterStatus().getDescription())
                .weight(animal.getWeight())
                .status(animal.getStatus().getDescription())
                .noticeStartDate(animal.getNoticeStartDate())
                .noticeEndDate(animal.getNoticeEndDate())
                .shelterName(animal.getShelterName())
                .shelterTel(animal.getShelterTel())
                .shelterAddress(animal.getShelterAddress())
                .province(animal.getProvince())
                .city(animal.getCity())
                .specialMark(animal.getSpecialMark())
                .foundPlace(animal.getFoundPlace())
                .color(animal.getColor())
                .healthInfo(animal.getHealthInfo())
                .vaccination(animal.getVaccination())
                .healthCheck(animal.getHealthCheck())
                .personality(animal.getPersonality())
                .build();
    }
}
