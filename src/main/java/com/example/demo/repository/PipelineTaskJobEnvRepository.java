package com.example.demo.repository;

import com.example.demo.entity.PipelineTaskJobEnv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PipelineTaskJobEnvRepository extends JpaRepository<PipelineTaskJobEnv, Long> {
    List<PipelineTaskJobEnv> findByPipelineJobId(long pipelineJobId);
}
