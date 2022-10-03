package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


// Todo: 값객체로 해도되지 않을까
@Entity
@Table(name = "pipeline_job_env")
@NoArgsConstructor
@Getter
public class PipelineTaskJobEnv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pipeline_job_id")
    private Long pipelineJobId;

    @Column(name = "pipeline_task_id")
    private Integer pipelineTaskId;

    @Column(name = "key_name")
    private String keyName;

    @Column(name = "value")
    private String value;

    public PipelineTaskJobEnv(long pipelineJobId, int pipelineTaskId, String key, String value) {
        this.pipelineJobId = pipelineJobId;
        this.pipelineTaskId = pipelineTaskId;
        this.keyName = key;
        this.value = value;
    }
}
