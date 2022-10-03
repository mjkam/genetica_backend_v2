package com.example.demo.converter;

import com.example.demo.entity.Pipeline;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class PipelineInputFileConverter implements AttributeConverter<List<Pipeline.InputFile>, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<Pipeline.InputFile> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Pipeline.InputFile> convertToEntityAttribute(String dbData) {
        try {
            return Arrays.asList(objectMapper.readValue(dbData, Pipeline.InputFile[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
