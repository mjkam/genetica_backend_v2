package com.example.demo.controller.dto;

import com.example.demo.entity.Pipeline;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jose4j.http.Get;

import java.util.List;

@NoArgsConstructor
@Getter
public class SavePipelineRequest {
    private String name;
    private PipelineRequest pipeline;

    @NoArgsConstructor
    @Getter
    public static class PipelineRequest {
        private List<Pipeline.Task> tasks;
        private List<Pipeline.InputFile> inputFiles;
        private List<Pipeline.OutputFile> outputFiles;
        private List<Pipeline.InputEdge> inputEdges;
        private List<Pipeline.ConnectEdge> connectEdges;
        private List<Pipeline.OutputEdge> outputEdges;
    }


}