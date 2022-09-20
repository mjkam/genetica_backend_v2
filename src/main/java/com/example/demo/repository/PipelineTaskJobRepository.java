package com.example.demo.repository;

import com.example.demo.entity.PipelineTaskJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PipelineTaskJobRepository extends JpaRepository<PipelineTaskJob, Long> {
    @Query("SELECT m FROM PipelineTaskJob m WHERE m.pipelineJob.id = :pipelineJobId")
    List<PipelineTaskJob> findByPipelineJobId(@Param("pipelineJobId") long pipelineJobId);
}
