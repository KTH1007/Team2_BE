package com.ganzi.backend.animal.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.AnimalType;
import com.ganzi.backend.animal.domain.NeuterStatus;
import com.ganzi.backend.animal.domain.ProcessState;
import com.ganzi.backend.animal.domain.Sex;
import com.ganzi.backend.animal.domain.repository.AnimalRepository;
import com.ganzi.backend.animal.infrastructure.dto.AnimalApiItem;
import com.ganzi.backend.animal.infrastructure.mapper.AnimalMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalSyncService 단위 테스트")
class AnimalSyncServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private AnimalMapper animalMapper;

    @InjectMocks
    private AnimalSyncService animalSyncService;

    @Captor
    private ArgumentCaptor<List<Animal>> animalListCaptor;

    @Test
    @DisplayName("신규 동물 데이터 저장 성공")
    void 신규_동물_데이터_저장_성공() {
        // given
        AnimalApiItem item = new AnimalApiItem();
        Animal animal = 테스트용_Animal_생성();

        given(animalRepository.existsById(any())).willReturn(false);
        given(animalMapper.toEntity(item)).willReturn(animal);

        // when
        int savedCount = animalSyncService.saveAnimalsInTransaction(List.of(item));

        // then
        assertThat(savedCount).isEqualTo(1);
        verify(animalRepository).saveAll(animalListCaptor.capture());
        assertThat(animalListCaptor.getValue()).hasSize(1);
        assertThat(animalListCaptor.getValue().get(0)).isEqualTo(animal);
    }

    @Test
    @DisplayName("이미 존재하는 동물은 스킵")
    void 이미_존재하는_동물은_스킵() {
        // given
        AnimalApiItem item = new AnimalApiItem();

        given(animalRepository.existsById(any())).willReturn(true);

        // when
        int savedCount = animalSyncService.saveAnimalsInTransaction(List.of(item));

        // then
        assertThat(savedCount).isEqualTo(0);
        verify(animalMapper, never()).toEntity(any());
        verify(animalRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("변환 실패해도 전체 동기화는 계속 진행")
    void 변환_실패해도_전체_동기화는_계속_진행() {
        // given
        AnimalApiItem item1 = new AnimalApiItem();
        AnimalApiItem item2 = new AnimalApiItem();
        AnimalApiItem item3 = new AnimalApiItem();

        Animal animal1 = 테스트용_Animal_생성();
        Animal animal3 = 테스트용_Animal_생성();

        given(animalRepository.existsById(any())).willReturn(false);
        given(animalMapper.toEntity(item1)).willReturn(animal1);
        given(animalMapper.toEntity(item2)).willThrow(new RuntimeException("변환 실패"));
        given(animalMapper.toEntity(item3)).willReturn(animal3);

        // when
        int savedCount = animalSyncService.saveAnimalsInTransaction(List.of(item1, item2, item3));

        // then
        assertThat(savedCount).isEqualTo(2);  // item2는 실패했지만 나머지는 성공
        verify(animalRepository).saveAll(animalListCaptor.capture());
        assertThat(animalListCaptor.getValue()).hasSize(2);
    }

    @Test
    @DisplayName("빈 리스트 전달 시 아무것도 저장하지 않음")
    void 빈_리스트_전달_시_아무것도_저장하지_않음() {
        // given
        List<AnimalApiItem> emptyList = List.of();

        // when
        int savedCount = animalSyncService.saveAnimalsInTransaction(emptyList);

        // then
        assertThat(savedCount).isEqualTo(0);
        verify(animalRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("여러 동물 중 일부만 신규인 경우")
    void 여러_동물_중_일부만_신규인_경우() {
        // given
        AnimalApiItem item1 = new AnimalApiItem();
        AnimalApiItem item2 = new AnimalApiItem();
        AnimalApiItem item3 = new AnimalApiItem();

        Animal animal2 = 테스트용_Animal_생성();

        given(animalRepository.existsById(any()))
                .willReturn(true)   // item1: 이미 존재
                .willReturn(false)  // item2: 신규
                .willReturn(true);  // item3: 이미 존재

        given(animalMapper.toEntity(item2)).willReturn(animal2);

        // when
        int savedCount = animalSyncService.saveAnimalsInTransaction(List.of(item1, item2, item3));

        // then
        assertThat(savedCount).isEqualTo(1);  // item2만 저장
        verify(animalMapper, times(1)).toEntity(any());  // item2만 변환
        verify(animalRepository).saveAll(animalListCaptor.capture());
        assertThat(animalListCaptor.getValue()).hasSize(1);
    }

    @Test
    @DisplayName("모든 동물이 이미 존재하는 경우")
    void 모든_동물이_이미_존재하는_경우() {
        // given
        AnimalApiItem item1 = new AnimalApiItem();
        AnimalApiItem item2 = new AnimalApiItem();

        given(animalRepository.existsById(any())).willReturn(true);

        // when
        int savedCount = animalSyncService.saveAnimalsInTransaction(List.of(item1, item2));

        // then
        assertThat(savedCount).isEqualTo(0);
        verify(animalMapper, never()).toEntity(any());
        verify(animalRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("모든 변환이 실패하는 경우")
    void 모든_변환이_실패하는_경우() {
        // given
        AnimalApiItem item1 = new AnimalApiItem();
        AnimalApiItem item2 = new AnimalApiItem();

        given(animalRepository.existsById(any())).willReturn(false);
        given(animalMapper.toEntity(any()))
                .willThrow(new RuntimeException("변환 실패"));

        // when
        int savedCount = animalSyncService.saveAnimalsInTransaction(List.of(item1, item2));

        // then
        assertThat(savedCount).isEqualTo(0);
        verify(animalRepository, never()).saveAll(any());
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
