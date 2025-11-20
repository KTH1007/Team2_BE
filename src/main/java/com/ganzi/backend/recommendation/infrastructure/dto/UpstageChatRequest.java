package com.ganzi.backend.recommendation.infrastructure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpstageChatRequest {

    private String model;
    private List<Message> messages;

    @Data
    @NoArgsConstructor
    public static class Message {
        private String role;    // "system", "user", "assistant"
        private String content;
    }
}
