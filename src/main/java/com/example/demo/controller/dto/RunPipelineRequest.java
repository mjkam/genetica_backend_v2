package com.example.demo.controller.dto;

import com.example.demo.service.PipelineInputUserFile;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class RunPipelineRequest {
    private Long pipelineId;
    private List<RequestFile> requestFiles;

    @NoArgsConstructor
    @Getter
    public static class RequestFile {
        private Integer inputFileId;
        private Long userFileId;
    }

    public List<PipelineInputUserFile> toInputUserFiles() {
        return this.requestFiles.stream()
                .map(o -> new PipelineInputUserFile(o.getInputFileId(), o.getUserFileId()))
                .collect(Collectors.toList());
    }
}
