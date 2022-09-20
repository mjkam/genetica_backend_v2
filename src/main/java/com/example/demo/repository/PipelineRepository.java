package com.example.demo.repository;

import com.example.demo.entity.Pipeline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipelineRepository extends JpaRepository<Pipeline, Long> {
}
