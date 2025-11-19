package com.ganzi.backend.animal.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ganzi.backend.animal.api.dto.request.AnimalSearchRequest;
import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.AnimalType;
import com.ganzi.backend.animal.domain.NeuterStatus;
import com.ganzi.backend.animal.domain.ProcessState;
import com.ganzi.backend.animal.domain.Sex;
import com.ganzi.backend.global.config.QueryDslConfig;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@Import(QueryDslConfig.class)
@DisplayName("AnimalRepository 통합 테스트")
class AnimalRepositoryTest {

    @Autowired
    private AnimalRepository animalRepository;

    private Animal testAnimal1;
    private Animal testAnimal2;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 준비
        testAnimal1 = Animal.builder()
                .desertionNo("REPO_TEST_001")
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

        testAnimal2 = Animal.builder()
                .desertionNo("REPO_TEST_002")
                .rfidCode("RFID002")
                .breedName("코리안숏헤어")
                .animalType(AnimalType.CAT)
                .age("2022(년생)")
                .sex(Sex.FEMALE)
                .neuterStatus(NeuterStatus.NO)
                .weight("4.2")
                .color("회색")
                .foundDate("20241105")
                .foundPlace("경기도 성남시")
                .noticeStartDate("20241106")
                .noticeEndDate("20241116")
                .status(ProcessState.PROTECTING)
                .shelterName("성남동물보호센터")
                .shelterTel("031-1234-5678")
                .shelterAddress("경기도 성남시 분당구 123")
                .province("경기도")
                .city("성남시")
                .specialMark("왼쪽 귀 끝 살짝 잘림")
                .healthInfo("양호")
                .vaccination("미완료")
                .healthCheck("완료")
                .personality("경계심 많음")
                .build();

        animalRepository.save(testAnimal1);
        animalRepository.save(testAnimal2);
    }

    @Test
    @DisplayName("ID로 동물 조회 성공")
    void ID로_동물_조회_성공() {
        // when
        Optional<Animal> result = animalRepository.findById("REPO_TEST_001");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getDesertionNo()).isEqualTo("REPO_TEST_001");
        assertThat(result.get().getBreedName()).isEqualTo("말티즈");
        assertThat(result.get().getAnimalType()).isEqualTo(AnimalType.DOG);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional 반환")
    void 존재하지_않는_ID로_조회_시_빈_Optional_반환() {
        // when
        Optional<Animal> result = animalRepository.findById("NOTFOUND");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("모든 동물 조회")
    void 모든_동물_조회() {
        // when
        List<Animal> animals = animalRepository.findAll();

        // then
        assertThat(animals).hasSize(2);
        assertThat(animals).extracting("breedName")
                .containsExactlyInAnyOrder("말티즈", "코리안숏헤어");
    }

    @Test
    @DisplayName("시도 목록 조회")
    void 시도_목록_조회() {
        // when
        List<String> provinces = animalRepository.findDistinctProvinces();

        // then
        assertThat(provinces).isNotEmpty();
        assertThat(provinces).contains("서울특별시", "경기도");
    }

    @Test
    @DisplayName("특정 시도의 시군구 목록 조회")
    void 특정_시도의_시군구_목록_조회() {
        // when
        List<String> cities = animalRepository.findDistinctCitesByProvince("서울특별시");

        // then
        assertThat(cities).isNotEmpty();
        assertThat(cities).contains("강남구");
    }

    @Test
    @DisplayName("검색 필터 - 동물 타입으로 필터링")
    void 검색_필터_동물_타입으로_필터링() {
        // given
        AnimalSearchRequest request = new AnimalSearchRequest(
                null, null, null, null, AnimalType.DOG, null, null, null, null
        );
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Animal> result = animalRepository.searchWithFilters(request, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getAnimalType()).isEqualTo(AnimalType.DOG);
        assertThat(result.getContent().get(0).getBreedName()).isEqualTo("말티즈");
    }

    @Test
    @DisplayName("검색 필터 - 성별로 필터링")
    void 검색_필터_성별로_필터링() {
        // given
        AnimalSearchRequest request = new AnimalSearchRequest(
                null, null, null, null, null, Sex.FEMALE, null, null, null
        );
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Animal> result = animalRepository.searchWithFilters(request, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getSex()).isEqualTo(Sex.FEMALE);
    }

    @Test
    @DisplayName("검색 필터 - 시도로 필터링")
    void 검색_필터_시도로_필터링() {
        // given
        AnimalSearchRequest request = new AnimalSearchRequest(
                null, null, "경기도", null, null, null, null, null, null
        );
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Animal> result = animalRepository.searchWithFilters(request, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getProvince()).isEqualTo("경기도");
        assertThat(result.getContent().get(0).getCity()).isEqualTo("성남시");
    }

    @Test
    @DisplayName("검색 필터 - 복합 조건 (동물 타입 + 성별)")
    void 검색_필터_복합_조건() {
        // given
        AnimalSearchRequest request = new AnimalSearchRequest(
                null, null, null, null, AnimalType.CAT, Sex.FEMALE, null, null, null
        );
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Animal> result = animalRepository.searchWithFilters(request, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getAnimalType()).isEqualTo(AnimalType.CAT);
        assertThat(result.getContent().get(0).getSex()).isEqualTo(Sex.FEMALE);
    }

    @Test
    @DisplayName("검색 필터 - 조건 없이 전체 조회")
    void 검색_필터_조건_없이_전체_조회() {
        // given
        AnimalSearchRequest request = new AnimalSearchRequest(
                null, null, null, null, null, null, null, null, null
        );
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Animal> result = animalRepository.searchWithFilters(request, pageable);

        // then
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("페이징 처리 검증")
    void 페이징_처리_검증() {
        // given
        AnimalSearchRequest request = new AnimalSearchRequest(
                null, null, null, null, null, null, null, null, null
        );
        Pageable pageable = PageRequest.of(0, 1);  // 페이지 크기 1

        // when
        Page<Animal> result = animalRepository.searchWithFilters(request, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.hasNext()).isTrue();
    }

    @Test
    @DisplayName("동물 저장 성공")
    void 동물_저장_성공() {
        // given
        Animal newAnimal = Animal.builder()
                .desertionNo("REPO_TEST_003")
                .rfidCode("RFID003")
                .breedName("리트리버")
                .animalType(AnimalType.DOG)
                .age("2021(년생)")
                .sex(Sex.MALE)
                .neuterStatus(NeuterStatus.YES)
                .weight("25.0")
                .color("골드")
                .foundDate("20241110")
                .foundPlace("인천시 남동구")
                .noticeStartDate("20241111")
                .noticeEndDate("20241121")
                .status(ProcessState.PROTECTING)
                .shelterName("인천동물보호센터")
                .shelterTel("032-1234-5678")
                .shelterAddress("인천시 남동구 123")
                .province("인천광역시")
                .city("남동구")
                .specialMark("목에 흰 털")
                .healthInfo("양호")
                .vaccination("완료")
                .healthCheck("완료")
                .personality("매우 활발함")
                .build();

        // when
        Animal saved = animalRepository.save(newAnimal);

        // then
        assertThat(saved.getDesertionNo()).isEqualTo("REPO_TEST_003");
        assertThat(animalRepository.findById("REPO_TEST_003")).isPresent();
        assertThat(animalRepository.count()).isEqualTo(3);
    }

    @Test
    @DisplayName("existsById로 존재 여부 확인")
    void existsById로_존재_여부_확인() {
        // when & then
        assertThat(animalRepository.existsById("REPO_TEST_001")).isTrue();
        assertThat(animalRepository.existsById("NOTFOUND")).isFalse();
    }
}
