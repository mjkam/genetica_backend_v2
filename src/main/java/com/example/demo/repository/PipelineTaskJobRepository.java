package com.example.demo.repository;

import com.example.demo.entity.PipelineTaskJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PipelineTaskJobRepository extends JpaRepository<PipelineTaskJob, Long> {
    @Query("SELECT m FROM PipelineTaskJob m WHERE m.pipelineJob.id = :pipelineJobId")
    List<PipelineTaskJob> findByPipelineJobId(@Param("pipelineJobId") long pipelineJobId);

    @Query("SELECT m FROM PipelineTaskJob m WHERE m.pipelineJob.id = :pipelineId AND m.pipelineTaskId = :pipelineTaskId")
    Optional<PipelineTaskJob> findOne(@Param("pipelineId") long pipelineId, @Param("pipelineTaskId") int pipelineTaskId);
}
