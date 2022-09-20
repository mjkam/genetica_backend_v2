package com.example.demo.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class PipelineJobInputUserFile {
    private Integer pipelineTaskId;
    private String pipelineTaskLabel;
    private Long fileId;
}
