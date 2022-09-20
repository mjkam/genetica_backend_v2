package com.example.demo.entity;

import com.example.demo.enums.JobStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "pipeline_job")
@NoArgsConstructor
@Getter
public class PipelineJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_id")
    private Pipeline pipeline;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    public PipelineJob(Pipeline pipeline) {
        this.pipeline = pipeline;
        this.status = JobStatus.PENDING;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.status = jobStatus;
    }
}
