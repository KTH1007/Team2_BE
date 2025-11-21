package com.ganzi.backend.animal.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("AnimalType Enum 테스트")
class AnimalTypeTest {

    @ParameterizedTest
    @CsvSource({
            "417000, DOG",
            "422400, CAT",
            "429900, ETC"
    })
    @DisplayName("정상 코드로 AnimalType 반환 성공")
    void 정상_코드로_변환_성공 (String code, AnimalType expected) {
        // when
        AnimalType result = AnimalType.fromCode(code);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"999999", "INVALID", "000000", "123456"})
    @DisplayName("알 수 없는 코드는 ETC 반환")
    void 알_수_없는_코드는_ETC_반환 (String unknownCode) {
        // when
        AnimalType result = AnimalType.fromCode(unknownCode);

        // then
        assertThat(result).isEqualTo(AnimalType.ETC);
    }

    @Test
    @DisplayName("null 코드는 ETC 반환")
    void null_코드는_ETC_반환() {
        // when
        AnimalType result = AnimalType.fromCode(null);

        // then
        assertThat(result).isEqualTo(AnimalType.ETC);
    }

    @Test
    @DisplayName("DOG 타입 정보 확인")
    void DOG_타입_정보_확인() {
        // given
        AnimalType dog = AnimalType.DOG;

        // then
        assertThat(dog.getCode()).isEqualTo("417000");
        assertThat(dog.getDescription()).isEqualTo("개");
    }

    @Test
    @DisplayName("CAT 타입 정보 확인")
    void CAT_타입_정보_확인() {
        // given
        AnimalType cat = AnimalType.CAT;

        // then
        assertThat(cat.getCode()).isEqualTo("422400");
        assertThat(cat.getDescription()).isEqualTo("고양이");
    }

    @Test
    @DisplayName("ETC 타입 정보 확인")
    void ETC_타입_정보_확인() {
        // given
        AnimalType etc = AnimalType.ETC;

        // then
        assertThat(etc.getCode()).isEqualTo("429900");
        assertThat(etc.getDescription()).isEqualTo("기타");
    }
}
