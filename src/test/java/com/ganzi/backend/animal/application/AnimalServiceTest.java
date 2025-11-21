package com.ganzi.backend.animal.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ganzi.backend.animal.api.dto.request.AnimalSearchRequest;
import com.ganzi.backend.animal.api.dto.response.AnimalDetailResponse;
import com.ganzi.backend.animal.api.dto.response.AnimalListResponse;
import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.AnimalType;
import com.ganzi.backend.animal.domain.NeuterStatus;
import com.ganzi.backend.animal.domain.ProcessState;
import com.ganzi.backend.animal.domain.Sex;
import com.ganzi.backend.animal.domain.repository.AnimalRepository;
import com.ganzi.backend.global.code.status.ErrorStatus;
import com.ganzi.backend.global.exception.GeneralException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalService 단위 테스트")
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private AnimalService animalService;

    @Test
    @DisplayName("동물 목록 조회 성공")
    void 동물_목록_조회_성공() {
        // given
        Animal animal = 테스트용_Animal_생성();
        List<Animal> animals = List.of(animal);
        Page<Animal> animalPage = new PageImpl<>(animals);
        Pageable pageable = PageRequest.of(0, 10);
        AnimalSearchRequest request = new AnimalSearchRequest(
                null, null, null, null, null, null, null, null, null
        );

        given(animalRepository.searchWithFilters(any(AnimalSearchRequest.class), any(Pageable.class)))
                .willReturn(animalPage);

        // when
        Page<AnimalListResponse> result = animalService.findAnimals(request, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).desertionNo()).isEqualTo("TEST001");
        assertThat(result.getContent().get(0).breedName()).isEqualTo("말티즈");
        verify(animalRepository).searchWithFilters(request, pageable);
    }

    @Test
    @DisplayName("동물 목록 조회 - 페이징 정보 확인")
    void 동물_목록_조회_페이징_정보_확인() {
        // given
        Animal animal1 = 테스트용_Animal_생성();
        Animal animal2 = 테스트용_Animal_생성();
        List<Animal> animalList = List.of(animal1, animal2);
        Page<Animal> animalPage = new PageImpl<>(animalList, PageRequest.of(0, 10), 2);
        AnimalSearchRequest request = new AnimalSearchRequest(
                null, null, null, null, null, null, null, null, null
        );

        given(animalRepository.searchWithFilters(any(), any()))
                .willReturn(animalPage);

        // when
        Page<AnimalListResponse> result = animalService.findAnimals(request, PageRequest.of(0, 10));

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("동물 목록 조회 - 빈 결과")
    void 동물_목록_조회_빈_결과() {
        // given
        Page<Animal> emptyPage = new PageImpl<>(List.of());
        AnimalSearchRequest request = new AnimalSearchRequest(
                null, null, null, null, null, null, null, null, null
        );

        given(animalRepository.searchWithFilters(any(), any()))
                .willReturn(emptyPage);

        // when
        Page<AnimalListResponse> result = animalService.findAnimals(request, PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("동물 상세 조회 성공")
    void 동물_상세_조회_성공() {
        // given
        String desertionNo = "TEST001";
        Animal animal = 테스트용_Animal_생성();

        given(animalRepository.findById(desertionNo))
                .willReturn(Optional.of(animal));

        // when
        AnimalDetailResponse result = animalService.findAnimalById(desertionNo);

        // then
        assertThat(result).isNotNull();
        assertThat(result.desertionNo()).isEqualTo(desertionNo);
        assertThat(result.breedName()).isEqualTo("말티즈");
        assertThat(result.province()).isEqualTo("서울특별시");
        verify(animalRepository).findById(desertionNo);
    }

    @Test
    @DisplayName("동물 상세 조회 실패 - 존재하지 않는 동물")
    void 동물_상세_조회_실패_존재하지_않는_동물() {
        // given
        String desertionNo = "NOTFOUND001";

        given(animalRepository.findById(desertionNo))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> animalService.findAnimalById(desertionNo))
                .isInstanceOf(GeneralException.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.ANIMAL_NOT_FOUND);

        verify(animalRepository).findById(desertionNo);
    }

    @Test
    @DisplayName("시도 목록 조회 성공")
    void 시도_목록_조회_성공() {
        // given
        List<String> expectedProvinces = List.of("서울특별시", "경기도", "인천광역시");

        given(animalRepository.findDistinctProvinces())
                .willReturn(expectedProvinces);

        // when
        List<String> result = animalService.findProvinces();

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly("서울특별시", "경기도", "인천광역시");
        verify(animalRepository).findDistinctProvinces();
    }

    @Test
    @DisplayName("시도 목록 조회 - 빈 결과")
    void 시도_목록_조회_빈_결과() {
        // given
        given(animalRepository.findDistinctProvinces())
                .willReturn(List.of());

        // when
        List<String> result = animalService.findProvinces();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("시군구 목록 조회 성공")
    void 시군구_목록_조회_성공() {
        // given
        String province = "서울특별시";
        List<String> expectedCities = List.of("강남구", "강동구", "강서구");

        given(animalRepository.findDistinctCitesByProvince(province))
                .willReturn(expectedCities);

        // when
        List<String> result = animalService.findCities(province);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly("강남구", "강동구", "강서구");
        verify(animalRepository).findDistinctCitesByProvince(province);
    }

    @Test
    @DisplayName("시군구 목록 조회 - 빈 결과")
    void 시군구_목록_조회_빈_결과() {
        // given
        String province = "제주특별자치도";
        given(animalRepository.findDistinctCitesByProvince(province))
                .willReturn(List.of());

        // when
        List<String> result = animalService.findCities(province);

        // then
        assertThat(result).isEmpty();
    }

    // 테스트 헬퍼 메서드
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
