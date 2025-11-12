package com.ganzi.backend.animal.api.dto.response;

import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.AnimalImage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "유기동물 리스트 조회")
@Builder
public record AnimalListResponse(
    @Schema(description = "구조번호")
    String desertionNo,

    @Schema(description = "동물등록번호")
    String rfidCode,

    @Schema(description = "품종명")
    String breedName,

    @Schema(description = "축종명")
    String animalTypeName,

    @Schema(description = "성별")
    String sex,

    @Schema(description = "중성화 여부")
    String neuterStatus,

    @Schema(description = "나이")
    String age,

    @Schema(description = "썸네일 이미지")
    String thumbnailImage,

    @Schema(description = "접수일")
    String foundDate,

    @Schema(description = "보호 상태")
    String status,

    @Schema(description = "시/도")
    String province,

    @Schema(description = "시/군/구")
    String city
) {
    public static AnimalListResponse from(Animal animal) {
        List<AnimalImage> images = animal.getImages();
        String thumbnailImage = null;
        if (!images.isEmpty()) {
            thumbnailImage = images.getFirst().getImageUrl();
        }

        return AnimalListResponse.builder()
                .desertionNo(animal.getDesertionNo())
                .rfidCode(animal.getRfidCode())
                .breedName(animal.getBreedName())
                .animalTypeName(animal.getAnimalType().getDescription())
                .sex(animal.getSex().getDescription())
                .neuterStatus(animal.getNeuterStatus().getDescription())
                .age(animal.getAge())
                .thumbnailImage(thumbnailImage)
                .foundDate(animal.getFoundDate())
                .status(animal.getStatus().getDescription())
                .province(animal.getProvince())
                .city(animal.getCity())
                .build();
    }
}
