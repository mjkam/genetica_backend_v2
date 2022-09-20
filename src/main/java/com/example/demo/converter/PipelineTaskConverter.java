package com.example.demo.converter;

import com.example.demo.entity.Pipeline;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class PipelineTaskConverter implements AttributeConverter<List<Pipeline.Task>, String> {
    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<Pipeline.Task> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Pipeline.Task> convertToEntityAttribute(String dbData) {
        try {
            return Arrays.asList(objectMapper.readValue(dbData, Pipeline.Task[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
