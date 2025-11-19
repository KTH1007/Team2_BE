package com.ganzi.backend.animal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Animal 도메인 엔티티 테스트")
class AnimalTest {

    @Test
    @DisplayName("Animal 엔티티 생성 성공")
    void Animal_엔티티_생성_성공 () {
        // given & when
        Animal animal = Animal.builder()
                .desertionNo("TEST001")
                .rfidCode("RFID001")
                .breedName("말티즈")
                .animalType(AnimalType.DOG)
                .age("2023(년생)")
                .sex(Sex.MALE)
                .neuterStatus(NeuterStatus.YES)
                .weight("3.5")
                .color("흰색")
                .foundDate("20241101")
                .foundPlace("서울시 강남구")
                .noticeStartDate("20241102")
                .noticeEndDate("20241112")
                .status(ProcessState.PROTECTING)
                .shelterName("강남동물보호센터")
                .shelterTel("02-1234-5678")
                .shelterAddress("서울시 강남구 테헤란로 123")
                .province("서울특별시")
                .city("강남구")
                .specialMark("온순함")
                .healthInfo("양호")
                .vaccination("완료")
                .healthCheck("완료")
                .personality("활발함")
                .build();

        // then
        assertThat(animal).isNotNull();
        assertThat(animal.getDesertionNo()).isEqualTo("TEST001");
        assertThat(animal.getBreedName()).isEqualTo("말티즈");
        assertThat(animal.getAnimalType()).isEqualTo(AnimalType.DOG);
        assertThat(animal.getSex()).isEqualTo(Sex.MALE);
        assertThat(animal.getImages()).isEmpty();
    }

    @Test
    @DisplayName("단일 이미지 추가 성공")
    void 단일_이미지_추가_성공() {
        // given
        Animal animal = 테스트용_Animal_생성();
        String imageUrl = "http://example.com/image1.jpg";
        Integer order = 1;

        // when
        animal.addImage(imageUrl, order);

        // then
        assertThat(animal.getImages()).hasSize(1);
        assertThat(animal.getImages().get(0).getImageUrl()).isEqualTo(imageUrl);
        assertThat(animal.getImages().get(0).getImageOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("단일 이미지 추가 시 양방향 연관관계 확인")
    void 단일_이미지_추가_시_양방향_연관관계_확인() {
        // given
        Animal animal = 테스트용_Animal_생성();
        String imageUrl = "http://example.com/image1.jpg";

        // when
        animal.addImage(imageUrl, 1);

        // then
        AnimalImage addedImage = animal.getImages().get(0);
        assertThat(addedImage.getAnimal()).isEqualTo(animal);
    }

    @Test
    @DisplayName("여러 이미지 추가 성공")
    void 여러_이미지_추가_성공() {
        // given
        Animal animal = 테스트용_Animal_생성();
        List<String> imageUrls = List.of(
                "http://example.com/image1.jpg",
                "http://example.com/image2.jpg",
                "http://example.com/image3.jpg"
        );

        // when
        animal.addImages(imageUrls);

        // then
        assertThat(animal.getImages()).hasSize(3);
    }

    @Test
    @DisplayName("여러 이미지 추가 시 순서가 1부터 올바르게 설정됨")
    void 여러_이미지_추가_시_순서가_1부터_올바르게_설정됨() {
        // given
        Animal animal = 테스트용_Animal_생성();
        List<String> imageUrls = List.of(
                "http://example.com/image1.jpg",
                "http://example.com/image2.jpg",
                "http://example.com/image3.jpg"
        );

        // when
        animal.addImages(imageUrls);

        // then
        assertThat(animal.getImages().get(0).getImageOrder()).isEqualTo(1);
        assertThat(animal.getImages().get(1).getImageOrder()).isEqualTo(2);
        assertThat(animal.getImages().get(2).getImageOrder()).isEqualTo(3);
    }

    @Test
    @DisplayName("여러 이미지 추가 시 URL이 순서대로 저장됨")
    void 여러_이미지_추가_시_URL이_순서대로_저장됨() {
        // given
        Animal animal = 테스트용_Animal_생성();
        List<String> imageUrls = List.of(
                "http://example.com/image1.jpg",
                "http://example.com/image2.jpg",
                "http://example.com/image3.jpg"
        );

        // when
        animal.addImages(imageUrls);

        // then
        assertThat(animal.getImages())
                .extracting("imageUrl")
                .containsExactly(
                        "http://example.com/image1.jpg",
                        "http://example.com/image2.jpg",
                        "http://example.com/image3.jpg"
                );
    }

    @Test
    @DisplayName("여러 이미지 추가 시 모든 이미지가 Animal과 연관관계를 가짐")
    void 여러_이미지_추가_시_모든_이미지가_Animal과_연관관계를_가짐() {
        // given
        Animal animal = 테스트용_Animal_생성();
        List<String> imageUrls = List.of(
                "http://example.com/image1.jpg",
                "http://example.com/image2.jpg"
        );

        // when
        animal.addImages(imageUrls);

        // then
        assertThat(animal.getImages())
                .allMatch(image -> image.getAnimal().equals(animal));
    }

    @Test
    @DisplayName("빈 이미지 리스트 추가 시 아무 변화 없음")
    void 빈_이미지_리스트_추가_시_아무_변화_없음() {
        // given
        Animal animal = 테스트용_Animal_생성();
        List<String> emptyList = List.of();

        // when
        animal.addImages(emptyList);

        // then
        assertThat(animal.getImages()).isEmpty();
    }

    // 테스트용 헬퍼 메서드
    private Animal 테스트용_Animal_생성() {
        return Animal.builder()
                .desertionNo("TEST001")
                .rfidCode("RFID001")
                .breedName("말티즈")
                .animalType(AnimalType.DOG)
                .age("2023(년생)")
                .sex(Sex.MALE)
                .neuterStatus(NeuterStatus.YES)
                .weight("3.5")
                .color("흰색")
                .foundDate("20241101")
                .foundPlace("서울시 강남구")
                .noticeStartDate("20241102")
                .noticeEndDate("20241112")
                .status(ProcessState.PROTECTING)
                .shelterName("강남동물보호센터")
                .shelterTel("02-1234-5678")
                .shelterAddress("서울시 강남구 테헤란로 123")
                .province("서울특별시")
                .city("강남구")
                .specialMark("온순함")
                .healthInfo("양호")
                .vaccination("완료")
                .healthCheck("완료")
                .personality("활발함")
                .build();
    }
}
