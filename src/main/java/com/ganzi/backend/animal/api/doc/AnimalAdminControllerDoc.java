package com.ganzi.backend.animal.api.doc;

import com.ganzi.backend.global.code.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "유기동물 관리", description = "유기동물 데이터 동기화 관리 API (관리자용)")
public interface AnimalAdminControllerDoc {

    @Operation(
            summary = "초기 동기화 실행",
            description = """
                    공공 API로부터 최근 7일치 유기동물 데이터를 동기화합니다.
                    
                    ### 실행 시점
                    - 서버 최초 배포 시 1회 실행
                    - DB가 비어있거나 초기 데이터가 필요할 때
                    
                    ### 동기화 범위
                    - 기간: 최근 7일
                    - 페이지 크기: 1000건씩
                    - 중복 체크: 구조번호(desertionNo) 기준으로 중복 제거
                    """
    )
    ResponseEntity<ApiResponse<String>> syncInitial();

    @Operation(
            summary = "수동 일일 동기화 실행",
            description = """
                    공공 API로부터 최근 1일치 유기동물 데이터를 수동으로 동기화합니다.
                    
                    ### 동기화 범위
                    - 기간: 최근 1일
                    - 페이지 크기: 1000건씩
                    - 중복 체크: 구조번호(desertionNo) 기준으로 중복 제거
                    
                    ### 자동 스케줄
                    - 매일 새벽 3시에 자동으로 동일한 동기화가 실행
                    - 수동 실행은 긴급 상황이나 보완 용도
                    """
    )
    ResponseEntity<ApiResponse<String>> syncDaily();
}
