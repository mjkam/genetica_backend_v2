package com.example.demo.repository;

import com.example.demo.entity.PipelineTaskJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskJobRepository extends JpaRepository<PipelineTaskJob, Long> {
}
