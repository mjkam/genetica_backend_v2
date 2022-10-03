package com.example.demo.entity;

import com.example.demo.enums.JobStatus;
import com.example.demo.utils.DateTimeFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pipeline_job")
@NoArgsConstructor
@Getter
public class PipelineJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pipeline_id")
    private Long pipelineId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(name = "created_datetime")
    private LocalDateTime createdDateTime;

    @Column(name = "start_datetime")
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime")
    private LocalDateTime endDateTime;

    public PipelineJob(long pipelineId, LocalDateTime currentDateTime) {
        this.pipelineId = pipelineId;
        this.status = JobStatus.PENDING;
        this.createdDateTime = currentDateTime;
        this.startDateTime = LocalDateTime.parse("3000-01-01 00:00:00", DateTimeFormat.formatter);
        this.endDateTime = LocalDateTime.parse("3000-01-01 00:00:00", DateTimeFormat.formatter);
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.status = jobStatus;
    }
}
