package com.ganzi.backend.recommendation.infrastructure;

import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.recommendation.application.RecommendationSummaryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInterestPromptFactory {

    public String buildSystemPrompt() {
        return String.join(" ",
                "너는 반려동물 입양을 도와주는 상담사야.",
                "아래에 주어진 동물 목록(색상, 품종, 동물종, 나이, 중성화 상태)을 보고, 사용자가 어떤 동물을 선호하는지 다음 조건을 완벽히 지켜서 한국어로 답변해.",
                "요청하지 않은 문장은 절대 생성하지 말 것.",
                "반드시 다음 JSON 형식으로만 응답해.",
                """
                        {
                          "data1": "...",
                          "data2": "..."
                        }""",
                """
                        data1에는 첫 번째 문장("{color}의 {breedName}을 좋아하시는군요!")만 넣고,
                        data2에는 나머지 3줄을 "\\n" 으로 구분해서 하나의 문자열로 넣어라.
                        추가적인 텍스트, 설명, 코드블록, 백틱은 절대 포함하지 말 것.
                        """,


                // 1줄 요약
                "첫 줄에는 사용자의 관심사에 저장된 동물들의 color와 breedName 중 가장 빈번하게 등장하는 조합 하나를 선택해서,",
                "'{color}의 {breedName}을 좋아하시는군요!' 형식으로 정확히 한 줄만 출력할 것.",
                "{color}가 여러개가 겹쳐서 여러 color를 출력해야한다면, 쉼표(,)를 붙여서 색끼리 구분할것",

                // 2~4번째 줄 요약
                "그 다음 줄부터는 다음의 내용을 정확히 3줄로, 줄바꿈으로 구분해서 출력할 것.",
                "줄 개수는 총 4줄이어야 하며, 그 이상도 이하도 안 된다.",
                "형식은 다음을 정확히 지켜야 한다.",

                // 2줄차
                "두 번째 줄: '선호 동물: {animalType} {아이콘} ({BreedName1, BreedName2})'",
                "여기서 {animalType}은 dog, cat 등 동물 타입을 한국어로 자연스럽게 표현하고,",
                "아이콘은 🐶, 🐱 등 간단한 이모지 한 개만 사용하며, 괄호 안에는 대표 품종 1~2개만 쉼표로 구분해서 넣어라.",

                // 3줄차
                "세 번째 줄: '평균 출생년도: {averageBirthYear}년 ({minAge}-{maxAge}세)'",
                "{averageBirthYear}는 대략적인 평균 출생년도(정수)로, age 정보를 바탕으로 합리적으로 추정한다.",
                "{minAge}와 {maxAge}는 사용자의 관심 목록에 있는 동물 나이의 최소/최대값(정수, 세 단위)으로 표현한다.",

                // 4줄차
                "네 번째 줄: '중성화 여부: {averageNeuterStatus}'",
                "{averageNeuterStatus}는 전체 동물을 보고, '대부분 중성화됨', '대부분 중성화 안 됨', '중성화 여부가 다양함' 등의 짧은 한국어 문장으로 요약한다.",

                // 추가 규칙
                "출력 전체에서 불필요한 설명 문장, 메타설명, 사족은 절대 붙이지 말고,",
                "반드시 위에서 정의한 4줄 형식만 출력할 것."
        );
    }

    public String buildUserContent(List<RecommendationSummaryService.WeightedAnimal> items) {
        StringBuilder sb = new StringBuilder();

        sb.append("아래는 사용자의 관심사에 저장된 동물 목록입니다.\n");
        sb.append("각 행은 'index | color | breedName | animalType | age | neuterStatus' 형식입니다.\n\n");

        int idx = 1;
        for (RecommendationSummaryService.WeightedAnimal item : items) {
            Animal animal = item.animal();

            sb.append(idx++).append(" | ");
            sb.append(nullToDash(animal.getColor())).append(" | ");
            sb.append(nullToDash(animal.getBreedName())).append(" | ");
            sb.append(nullToDash(animal.getAnimalType() != null ? animal.getAnimalType().name() : null)).append(" | ");
            sb.append(nullToDash(animal.getAge())).append(" | ");
            sb.append(nullToDash(animal.getNeuterStatus() != null ? animal.getNeuterStatus().name() : null));
            sb.append("\n");
        }

        return sb.toString();
    }

    private String nullToDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
