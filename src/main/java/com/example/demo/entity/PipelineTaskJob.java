package com.example.demo.entity;

import com.example.demo.enums.JobStatus;
import com.example.demo.utils.DateTimeFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pipeline_task_job")
@NoArgsConstructor
@Getter
public class PipelineTaskJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pipeline_job_id")
    private Long pipelineJobId;

    @Column(name = "pipeline_task_id")
    private Integer pipelineTaskId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(name = "created_datetime")
    private LocalDateTime createdDateTime;

    @Column(name = "start_datetime")
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime")
    private LocalDateTime endDateTime;

    public PipelineTaskJob(long pipelineJobId, int pipelineTaskId, LocalDateTime currentDateTime) {
        this.pipelineJobId = pipelineJobId;
        this.pipelineTaskId = pipelineTaskId;
        this.status = JobStatus.PENDING;
        this.createdDateTime = currentDateTime;
        this.startDateTime = LocalDateTime.parse("3000-01-01 00:00:00", DateTimeFormat.formatter);
        this.endDateTime = LocalDateTime.parse("3000-01-01 00:00:00", DateTimeFormat.formatter);
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
