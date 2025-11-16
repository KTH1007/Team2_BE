package com.ganzi.backend.rag.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


public record UpstageApiResponse(List<Choice> choices){

    public static record Choice(
        Message message,

        @JsonProperty("finish_reason")
        String finishReason
    ){}


    public static record Message (
        String role,
        String content
    ){}
}
