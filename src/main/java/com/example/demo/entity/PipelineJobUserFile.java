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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_job_id")
    private PipelineJob pipelineJob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_file_id")
    private UserFile userFile;

    @Column(name = "pipeline_task_id")
    private Integer pipelineTaskId;

    @Column(name = "relation_type")
    @Enumerated(EnumType.STRING)
    private PipelineJobUserFileRelationType pipelineJobUserFileRelationType;

    @Column(name = "label")
    private String label;

    public PipelineJobUserFile(PipelineJob pipelineJob, UserFile userFile, String label, PipelineJobUserFileRelationType pipelineJobUserFileRelationType, int pipelineTaskId) {
        this.pipelineJob = pipelineJob;
        this.userFile = userFile;
        this.label = label;
        this.pipelineJobUserFileRelationType = pipelineJobUserFileRelationType;
        this.pipelineTaskId = pipelineTaskId;
    }
}
