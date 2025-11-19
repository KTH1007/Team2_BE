package com.ganzi.backend.animal.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class NeuterStatusTest {
    @ParameterizedTest
    @CsvSource({
            "Y, YES",
            "N, NO",
            "U, UNKNOWN"
    })
    @DisplayName("정상 코드로 NeuterStatus 변환 성공")
    void 정상_코드로_변환_성공(String code, NeuterStatus expected) {
        // when
        NeuterStatus result = NeuterStatus.fromCode(code);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"X", "INVALID", "y", "n", "0", "1"})
    @DisplayName("알 수 없는 코드는 UNKNOWN 반환")
    void 알_수_없는_코드는_UNKNOWN_반환(String unknownCode) {
        // when
        NeuterStatus result = NeuterStatus.fromCode(unknownCode);

        // then
        assertThat(result).isEqualTo(NeuterStatus.UNKNOWN);
    }

    @Test
    @DisplayName("null 코드는 UNKNOWN 반환")
    void null_코드는_UNKNOWN_반환() {
        // when
        NeuterStatus result = NeuterStatus.fromCode(null);

        // then
        assertThat(result).isEqualTo(NeuterStatus.UNKNOWN);
    }

    @Test
    @DisplayName("YES 정보 확인")
    void YES_정보_확인() {
        // given
        NeuterStatus yes = NeuterStatus.YES;

        // then
        assertThat(yes.getCode()).isEqualTo("Y");
        assertThat(yes.getDescription()).isEqualTo("중성화 완료");
    }

    @Test
    @DisplayName("NO 정보 확인")
    void NO_정보_확인() {
        // given
        NeuterStatus no = NeuterStatus.NO;

        // then
        assertThat(no.getCode()).isEqualTo("N");
        assertThat(no.getDescription()).isEqualTo("중성화 안 함");
    }

    @Test
    @DisplayName("UNKNOWN 정보 확인")
    void UNKNOWN_정보_확인() {
        // given
        NeuterStatus unknown = NeuterStatus.UNKNOWN;

        // then
        assertThat(unknown.getCode()).isEqualTo("U");
        assertThat(unknown.getDescription()).isEqualTo("미상");
    }
}
