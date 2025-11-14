package com.ganzi.backend.rag.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpstageApiResponse {

    private List<Choice> choices;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private Message message;

        @JsonProperty("finish_reason")
        private String finishReason;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
