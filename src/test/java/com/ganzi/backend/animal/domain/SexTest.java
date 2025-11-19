package com.ganzi.backend.animal.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Sex Enum 테스트")
class SexTest {

    @ParameterizedTest
    @CsvSource({
            "M, MALE",
            "F, FEMALE",
            "Q, UNKNOWN"
    })
    @DisplayName("정상 코드로 Sex 변환 성공")
    void 정상_코드로_변환_성공(String code, Sex expected) {
        // when
        Sex result = Sex.fromCode(code);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"X", "U", "INVALID", "m", "f"})
    @DisplayName("알 수 없는 코드는 UNKNOWN 반환")
    void 알_수_없는_코드는_UNKNOWN_반환(String unknownCode) {
        // when
        Sex result = Sex.fromCode(unknownCode);

        // then
        assertThat(result).isEqualTo(Sex.UNKNOWN);
    }

    @Test
    @DisplayName("MALE 정보 확인")
    void MALE_정보_확인() {
        // given
        Sex male = Sex.MALE;

        // then
        assertThat(male.getCode()).isEqualTo("M");
        assertThat(male.getDescription()).isEqualTo("수컷");
    }

    @Test
    @DisplayName("FEMALE 정보 확인")
    void FEMALE_정보_확인() {
        // given
        Sex female = Sex.FEMALE;

        // then
        assertThat(female.getCode()).isEqualTo("F");
        assertThat(female.getDescription()).isEqualTo("암컷");
    }

    @Test
    @DisplayName("UNKNOWN 정보 확인")
    void UNKNOWN_정보_확인() {
        // given
        Sex unknown = Sex.UNKNOWN;

        // then
        assertThat(unknown.getCode()).isEqualTo("Q");
        assertThat(unknown.getDescription()).isEqualTo("미상");
    }
}
