package com.example.demo.repository;

import com.example.demo.entity.PipelineTaskJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PipelineTaskJobRepository extends JpaRepository<PipelineTaskJob, Long> {
    @Query("SELECT m FROM PipelineTaskJob m WHERE m.pipelineJobId = :pipelineJobId AND m.pipelineTaskId = :taskId")
    Optional<PipelineTaskJob> findOne(@Param("pipelineJobId") long pipelineJobId, @Param("taskId") int taskId);

    List<PipelineTaskJob> findByPipelineJobId(long pipelineJobId);
}
