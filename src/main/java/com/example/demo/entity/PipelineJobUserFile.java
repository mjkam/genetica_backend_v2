package com.example.demo.entity;

import com.example.demo.enums.PipelineJobUserFileRelationType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "pipeline_job_user_file")
@NoArgsConstructor
@Getter
public class PipelineJobUserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pipeline_job_id")
    private Long pipelineJobId;

    @Column(name = "pipeline_task_id")
    private Integer pipelineTaskId;

    @Column(name = "pipeline_task_port_id")
    private Integer pipelineTaskPortId;

    @Column(name = "user_file_id")
    private Long userFileId;

    @Column(name = "relation_type")
    @Enumerated(EnumType.STRING)
    private PipelineJobUserFileRelationType pipelineJobUserFileRelationType;

    public PipelineJobUserFile(long pipelineJobId, int pipelineTaskId, int pipelineTaskPortId, long userFileId, PipelineJobUserFileRelationType pipelineJobUserFileRelationType) {
        this.pipelineJobId = pipelineJobId;
        this.pipelineTaskId = pipelineTaskId;
        this.pipelineTaskPortId = pipelineTaskPortId;
        this.userFileId = userFileId;
        this.pipelineJobUserFileRelationType = pipelineJobUserFileRelationType;
    }
}
