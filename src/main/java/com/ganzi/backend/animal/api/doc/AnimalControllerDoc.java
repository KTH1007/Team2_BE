package com.ganzi.backend.animal.api.doc;

import com.ganzi.backend.animal.api.dto.request.AnimalSearchRequest;
import com.ganzi.backend.animal.api.dto.response.AnimalDetailResponse;
import com.ganzi.backend.animal.api.dto.response.AnimalListResponse;
import com.ganzi.backend.global.code.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "유기동물", description = "유기동물 조회 API")
public interface AnimalControllerDoc {

    @Operation(
            summary = "유기동물 리스트 조회",
            description = """
                    필터링 및 페이징을 통해 유기동물 목록을 조회합니다.
                    
                    ### 필터 조건
                    
                    **기간 설정 (기본 3개월)**
                    - `startDate`: 검색 시작일 (yyyyMMdd 형식, 예: 20250809)
                    - `endDate`: 검색 종료일 (yyyyMMdd 형식, 예: 20251109)
                    - 둘 다 입력하지 않으면 기본 검색 기간 적용
                    
                    **지역 설정**
                    - `province`: 시/도 (예: 서울특별시, 경기도, 전라남도)
                    - `city`: 시/군/구 (예: 강남구, 성남시, 순천시)
                    
                    **동물 정보**
                    - `animalType`: 축종 코드
                      - `DOG`: 개
                      - `CAT`: 고양이
                      - `ETC`: 기타
                    - `sex`: 성별
                      - `MALE`: 수컷
                      - `FEMALE`: 암컷
                      - `UNKNOWN`: 미상
                    - `neuterStatus`: 중성화 여부
                      - `YES`: 중성화 완료
                      - `NO`: 중성화 안 함
                      - `UNKNOWN`: 미상
                    
                    **추가 옵션**
                    - `onlyProtecting`: 보호중인 동물만 표시 (true/false)
                    - `isLatest`: 정렬 순서 (true: 최신순, false: 오래된순)
                    
                    ### 페이징
                    - 한 페이지당 20개씩 표시
                    - `page`: 페이지 번호 (0부터 시작)
                    - `size`: 페이지 크기 (기본 20개)
                    - `sort`: 정렬 기준 (예: foundDate,desc)
                    """
    )
    ResponseEntity<ApiResponse<Page<AnimalListResponse>>> findAnimals(
            @Parameter(
                    description = """
                            유기동물 검색 필터 조건
                            - 모든 파라미터는 선택사항입니다
                            - 입력하지 않은 필터는 전체 조회로 처리됩니다
                            """
            )
            @ModelAttribute AnimalSearchRequest request,
            @Parameter(
                    description = """
                            페이징 정보
                            - page: 페이지 번호 (0부터 시작, 기본값: 0)
                            - size: 페이지 크기 (기본값: 20)
                            - sort: 정렬 기준 (예: foundDate,desc)
                            """
            )
            Pageable pageable
    );

    @Operation(
            summary = "유기동물 상세 조회",
            description = """
                    구조번호(desertionNo)로 유기동물의 상세 정보를 조회합니다.
                    
                    ### 응답 데이터
                    
                    **기본 정보**
                    - 구조번호, 동물등록번호, 품종명, 축종명
                    - 성별, 중성화 여부, 나이, 체중, 색상
                    
                    **이미지**
                    - 전체 이미지 URL 리스트 (최대 8개)
                    
                    **발견 정보**
                    - 접수일, 발견 장소
                    
                    **공고 정보**
                    - 보호 상태, 공고 시작일, 공고 종료일
                    
                    **보호소 정보**
                    - 보호소 이름, 전화번호, 주소
                    - 관할 기관 (시/도, 시/군/구)
                    
                    **건강 및 특징**
                    - 특징 (specialMark)
                    - 건강 정보 (healthInfo)
                    - 백신 접종 정보 (vaccination)
                    - 건강 체크 정보 (healthCheck)
                    - 성격/사회성 (personality)
                    """
    )
    ResponseEntity<ApiResponse<AnimalDetailResponse>> findAnimalById(
            @Parameter(description = "유기동물 구조번호 (desertionNo)")
            @PathVariable String desertionNo
    );
}
