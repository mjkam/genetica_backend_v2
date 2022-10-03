package com.example.demo.repository;

import com.example.demo.entity.PipelineJobUserFile;
import com.example.demo.enums.PipelineJobUserFileRelationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PipelineJobUserFileRepository extends JpaRepository<PipelineJobUserFile, Long> {
    @Query("SELECT m FROM PipelineJobUserFile m WHERE m.pipelineJobId = :pipelineJobId AND m.pipelineJobUserFileRelationType = :relationType")
    List<PipelineJobUserFile> findPipelineInputUserFiles(@Param("pipelineJobId") long pipelineJobId, @Param("relationType")PipelineJobUserFileRelationType relationType);
}
