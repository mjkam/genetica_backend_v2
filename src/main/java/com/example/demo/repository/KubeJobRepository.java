package com.example.demo.repository;

import com.example.demo.entity.KubeJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KubeJobRepository extends JpaRepository<KubeJob, Long> {
    @Query("SELECT m FROM KubeJob m WHERE m.pipelineTaskJobId = :taskJobId AND m.sequence = :sequence")
    Optional<KubeJob> findNextJob(@Param("taskJobId") long taskJobId, int sequence);
}
