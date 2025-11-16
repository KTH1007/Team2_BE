package com.ganzi.backend.user.domain.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonMapper {
    private static final ObjectMapper OBJECT_MAPPER = create();

    private JsonMapper() {
    }

    private static ObjectMapper create() {
        ObjectMapper mapper = new ObjectMapper();
        // LocalDateTime 등 Java Time 모듈 등록
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }
}
