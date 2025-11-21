package com.ganzi.backend.animal.infrastructure.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.AnimalType;
import com.ganzi.backend.animal.domain.NeuterStatus;
import com.ganzi.backend.animal.domain.ProcessState;
import com.ganzi.backend.animal.domain.Sex;
import com.ganzi.backend.animal.infrastructure.dto.AnimalApiItem;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AnimalMapper 테스트")
class AnimalMapperTest {

    private AnimalMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AnimalMapper();
    }

    @Test
    @DisplayName("정상 데이터 변환 성공")
    void 정상_데이터_변환_성공() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();

        // then
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal).isNotNull();
        assertThat(animal.getDesertionNo()).isEqualTo("TEST001");
        assertThat(animal.getRfidCode()).isEqualTo("RFID001");
        assertThat(animal.getBreedName()).isEqualTo("말티즈");
        assertThat(animal.getAnimalType()).isEqualTo(AnimalType.DOG);
        assertThat(animal.getAge()).isEqualTo("2023(년생)");
        assertThat(animal.getSex()).isEqualTo(Sex.MALE);
        assertThat(animal.getNeuterStatus()).isEqualTo(NeuterStatus.YES);
        assertThat(animal.getWeight()).isEqualTo("3.5");
        assertThat(animal.getColor()).isEqualTo("흰색");
        assertThat(animal.getFoundDate()).isEqualTo("20241101");
        assertThat(animal.getFoundPlace()).isEqualTo("서울시 강남구");
        assertThat(animal.getNoticeStartDate()).isEqualTo("20241102");
        assertThat(animal.getNoticeEndDate()).isEqualTo("20241112");
        assertThat(animal.getStatus()).isEqualTo(ProcessState.PROTECTING);
        assertThat(animal.getShelterName()).isEqualTo("강남동물보호센터");
        assertThat(animal.getShelterTel()).isEqualTo("02-1234-5678");
        assertThat(animal.getShelterAddress()).isEqualTo("서울시 강남구 테헤란로 123");
        assertThat(animal.getProvince()).isEqualTo("서울특별시");
        assertThat(animal.getCity()).isEqualTo("강남구");
    }

    @Test
    @DisplayName("잘못된 AnimalType 코드는 ETC로 변환")
    void 잘못된_AnimalType_코드는_ETC로_변환() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "upKindCd", "INVALID_CODE");

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getAnimalType()).isEqualTo(AnimalType.ETC);
    }

    @Test
    @DisplayName("잘못된 Sex 코드는 UNKNOWN으로 변환")
    void 잘못된_Sex_코드는_UNKNOWN으로_변환() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "sexCd", "X");

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getSex()).isEqualTo(Sex.UNKNOWN);
    }

    @Test
    @DisplayName("잘못된 NeuterStatus 코드는 UNKNOWN으로 변환")
    void 잘못된_NeuterStatus_코드는_UNKNOWN으로_변환() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "neuterYn", "X");

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getNeuterStatus()).isEqualTo(NeuterStatus.UNKNOWN);
    }

    @Test
    @DisplayName("잘못된 ProcessState 값은 PROTECTING으로 변환")
    void 잘못된_ProcessState_값은_PROTECTING으로_변환() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "processState", "INVALID_STATE");

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getStatus()).isEqualTo(ProcessState.PROTECTING);
    }

    @Test
    @DisplayName("orgNm이 null이면 province와 city도 null")
    void orgNm이_null이면_province와_city도_null() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "orgNm", null);

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getProvince()).isNull();
        assertThat(animal.getCity()).isNull();
    }

    @Test
    @DisplayName("orgNm이 빈 문자열이면 province와 city도 null")
    void orgNm이_빈_문자열이면_province와_city도_null() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "orgNm", "");

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getProvince()).isNull();
        assertThat(animal.getCity()).isNull();
    }

    @Test
    @DisplayName("orgNm에서 province와 city 정상 추출")
    void orgNm에서_province와_city_정상_추출() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "orgNm", "서울특별시 강남구");

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getProvince()).isEqualTo("서울특별시");
        assertThat(animal.getCity()).isEqualTo("강남구");
    }

    @Test
    @DisplayName("orgNm에 province만 있으면 city는 null")
    void orgNm에_province만_있으면_city는_null() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "orgNm", "서울특별시");

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getProvince()).isEqualTo("서울특별시");
        assertThat(animal.getCity()).isNull();
    }

    @Test
    @DisplayName("이미지 URL이 모두 유효하면 전부 추가")
    void 이미지_URL이_모두_유효하면_전부_추가() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "popfile1", "http://image1.jpg");
        setField(item, "popfile2", "http://image2.jpg");
        setField(item, "popfile3", "http://image3.jpg");

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getImages()).hasSize(3);
        assertThat(animal.getImages().get(0).getImageUrl()).isEqualTo("http://image1.jpg");
        assertThat(animal.getImages().get(1).getImageUrl()).isEqualTo("http://image2.jpg");
        assertThat(animal.getImages().get(2).getImageUrl()).isEqualTo("http://image3.jpg");
    }

    @Test
    @DisplayName("null 이미지 URL은 필터링됨")
    void null_이미지_URL은_필터링됨() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "popfile1", "http://image1.jpg");
        setField(item, "popfile2", null);
        setField(item, "popfile3", "http://image3.jpg");

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getImages()).hasSize(2);
        assertThat(animal.getImages())
                .extracting("imageUrl")
                .containsExactly("http://image1.jpg", "http://image3.jpg");
    }

    @Test
    @DisplayName("빈 문자열 이미지 URL은 필터링됨")
    void 빈_문자열_이미지_URL은_필터링됨() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "popfile1", "http://image1.jpg");
        setField(item, "popfile2", "");
        setField(item, "popfile3", "http://image3.jpg");

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getImages()).hasSize(2);
    }

    @Test
    @DisplayName("공백 이미지 URL은 필터링됨")
    void 공백_이미지_URL은_필터링됨() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "popfile1", "http://image1.jpg");
        setField(item, "popfile2", "   ");
        setField(item, "popfile3", "http://image3.jpg");

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getImages()).hasSize(2);
    }

    @Test
    @DisplayName("이미지 순서가 1부터 올바르게 설정됨")
    void 이미지_순서가_1부터_올바르게_설정됨() throws Exception {
        // given
        AnimalApiItem item = 정상_데이터_생성();
        setField(item, "popfile1", "http://image1.jpg");
        setField(item, "popfile2", "http://image2.jpg");
        setField(item, "popfile3", "http://image3.jpg");

        // when
        Animal animal = mapper.toEntity(item);

        // then
        assertThat(animal.getImages().get(0).getImageOrder()).isEqualTo(1);
        assertThat(animal.getImages().get(1).getImageOrder()).isEqualTo(2);
        assertThat(animal.getImages().get(2).getImageOrder()).isEqualTo(3);
    }

    // 테스트 헬퍼 메서드

    private AnimalApiItem 정상_데이터_생성() throws Exception {
        AnimalApiItem item = new AnimalApiItem();

        setField(item, "desertionNo", "TEST001");
        setField(item, "rfidCd", "RFID001");
        setField(item, "kindNm", "말티즈");
        setField(item, "upKindCd", "417000");
        setField(item, "age", "2023(년생)");
        setField(item, "sexCd", "M");
        setField(item, "neuterYn", "Y");
        setField(item, "weight", "3.5");
        setField(item, "colorCd", "흰색");
        setField(item, "happenDt", "20241101");
        setField(item, "happenPlace", "서울시 강남구");
        setField(item, "noticeSdt", "20241102");
        setField(item, "noticeEdt", "20241112");
        setField(item, "processState", "보호중");
        setField(item, "careNm", "강남동물보호센터");
        setField(item, "careTel", "02-1234-5678");
        setField(item, "careAddr", "서울시 강남구 테헤란로 123");
        setField(item, "orgNm", "서울특별시 강남구");
        setField(item, "specialMark", "온순함");
        setField(item, "sfeHealth", "양호");
        setField(item, "vaccinationChk", "완료");
        setField(item, "healthChk", "완료");
        setField(item, "sfeSoci", "활발함");

        return item;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
