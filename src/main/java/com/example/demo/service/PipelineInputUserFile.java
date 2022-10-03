package com.example.demo.service;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PipelineInputUserFile {
    private Integer inputFileId;
    private Long userFileId;

    public PipelineInputUserFile(int inputFileId, long userFileId) {
        this.inputFileId = inputFileId;
        this.userFileId = userFileId;
    }
}
