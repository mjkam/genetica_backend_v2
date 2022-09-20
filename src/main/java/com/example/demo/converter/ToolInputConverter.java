package com.example.demo.converter;

import com.example.demo.entity.Tool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class ToolInputConverter implements AttributeConverter<List<Tool.Input>, String> {
    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<Tool.Input> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Tool.Input> convertToEntityAttribute(String dbData) {
        try {
            return Arrays.asList(objectMapper.readValue(dbData, Tool.Input[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
