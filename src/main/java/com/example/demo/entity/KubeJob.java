package com.example.demo.entity;

import com.example.demo.enums.JobStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "kube_job")
@NoArgsConstructor
@Getter
public class KubeJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_job_id")
    private PipelineTaskJob pipelineTaskJob;

    @Column(name = "sequence_id")
    private Integer sequenceId;

    @Column(name = "image")
    private String image;

    @Column(name = "command")
    private String command;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    public KubeJob(PipelineTaskJob pipelineTaskJob, int sequenceId, String command, String image) {
        this.pipelineTaskJob = pipelineTaskJob;
        this.sequenceId = sequenceId;
        this.command = command;
        this.image = image;
        this.status = JobStatus.QUEUED;
    }

    public void setKubeJobStatus(JobStatus jobStatus) {
        this.status = jobStatus;
    }
}
