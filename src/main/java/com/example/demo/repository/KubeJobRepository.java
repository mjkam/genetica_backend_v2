package com.example.demo.repository;

import com.example.demo.entity.KubeJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KubeJobRepository extends JpaRepository<KubeJob, Long> {
    @Query("SELECT m FROM KubeJob m WHERE m.pipelineTaskJob.pipelineJob.id = :pipelineJobId AND m.pipelineTaskJob.pipelineTaskId = :pipelineTaskId AND m.sequenceId = :sequenceId")
    Optional<KubeJob> findNextJob(@Param("pipelineJobId") long pipelineJobId, @Param("pipelineTaskId") int pipelineTaskId, @Param("sequenceId") int sequenceId);
}
