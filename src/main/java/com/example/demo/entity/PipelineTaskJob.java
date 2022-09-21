package com.example.demo.entity;

import com.example.demo.enums.JobStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "task_job")
@NoArgsConstructor
@Getter
public class PipelineTaskJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_job_id")
    private PipelineJob pipelineJob;

    @Column(name = "pipeline_task_id")
    private Integer pipelineTaskId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    public PipelineTaskJob(PipelineJob pipelineJob, int pipelineTaskId) {
        this.pipelineJob = pipelineJob;
        this.pipelineTaskId = pipelineTaskId;
        this.status = JobStatus.PENDING;
    }

    public boolean isSuccess() {
        return this.status.equals(JobStatus.SUCCESS);
    }
    public boolean isFinished() {
        if (this.status.equals(JobStatus.SUCCESS) || this.status.equals(JobStatus.FAILED)) {
            return true;
        }
        return false;
    }

    public void setTaskJobStatus(JobStatus jobStatus) {
        this.status = jobStatus;
    }
}
