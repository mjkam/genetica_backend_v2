package com.example.demo.entity;

import com.example.demo.enums.JobStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "kube_job")
@NoArgsConstructor
@Getter
@ToString
public class KubeJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pipeline_task_job_id")
    private Long pipelineTaskJobId;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "image")
    private String image;

    @Column(name = "command")
    private String command;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    public KubeJob(long pipelineTaskJobId, int sequence, String image, String command) {
        this.pipelineTaskJobId = pipelineTaskJobId;
        this.sequence = sequence;
        this.command = command;
        this.image = image;
        this.status = JobStatus.QUEUED;
    }

    public void setKubeJobStatus(JobStatus jobStatus) {
        this.status = jobStatus;
    }
}
