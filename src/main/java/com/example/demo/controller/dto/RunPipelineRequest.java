package com.example.demo.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class RunPipelineRequest {
    private Long pipelineId;
    private List<RequestFile> requestFiles;

    @NoArgsConstructor
    @Getter
    public static class RequestFile {
        private Integer taskId;
        private String taskInputLabel;
        private Long fileId;
    }
}
