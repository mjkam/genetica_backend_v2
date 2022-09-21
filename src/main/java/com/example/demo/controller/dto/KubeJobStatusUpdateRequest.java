package com.example.demo.controller.dto;

import com.example.demo.enums.JobStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class KubeJobStatusUpdateRequest {
    private Long pipelineJobId;
    private Integer pipelineTaskId;
    private Long kubeJobId;
    private JobStatus jobStatus;
}
