package com.example.demo.repository;

import com.example.demo.entity.PipelineJobEnv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PipelineJobEnvRepository extends JpaRepository<PipelineJobEnv, Long> {
    @Query("SELECT m FROM PipelineJobEnv m WHERE m.pipelineJob.id = :pipelineJobId")
    List<PipelineJobEnv> findPipelineJobEnvs(@Param("pipelineJobId") long pipelineJobId);
}
