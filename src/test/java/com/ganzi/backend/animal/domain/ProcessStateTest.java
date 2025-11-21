package com.ganzi.backend.animal.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ProcessState Enum 테스트")
class ProcessStateTest {

    @ParameterizedTest
    @CsvSource({
            "보호중, PROTECTING",
            "종료(입양), ADOPTED",
            "종료(자연사), NATURAL_DEATH",
            "종료(안락사), EUTHANIZED",
            "종료(반환), RETURNED"
    })
    @DisplayName("정상 API 값으로 ProcessState 변환 성공")
    void 정상_API_값으로_변환_성공(String apiValue, ProcessState expected) {
        // when
        ProcessState result = ProcessState.fromApiValue(apiValue);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "종료", "완료", "Unknown", ""})
    @DisplayName("알 수 없는 값은 PROTECTING 반환")
    void 알_수_없는_값은_PROTECTING_반환(String unknownValue) {
        // when
        ProcessState result = ProcessState.fromApiValue(unknownValue);

        // then
        assertThat(result).isEqualTo(ProcessState.PROTECTING);
    }

    @Test
    @DisplayName("null 값은 PROTECTING 반환")
    void null_값은_PROTECTING_반환() {
        // when
        ProcessState result = ProcessState.fromApiValue(null);

        // then
        assertThat(result).isEqualTo(ProcessState.PROTECTING);
    }

    @Test
    @DisplayName("PROTECTING 정보 확인")
    void PROTECTING_정보_확인() {
        // given
        ProcessState protecting = ProcessState.PROTECTING;

        // then
        assertThat(protecting.getApiValue()).isEqualTo("보호중");
        assertThat(protecting.getDescription()).isEqualTo("보호중");
    }

    @Test
    @DisplayName("ADOPTED 정보 확인")
    void ADOPTED_정보_확인() {
        // given
        ProcessState adopted = ProcessState.ADOPTED;

        // then
        assertThat(adopted.getApiValue()).isEqualTo("종료(입양)");
        assertThat(adopted.getDescription()).isEqualTo("입양완료");
    }
}
