package com.example.demo.repository;

import com.example.demo.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserFileRepository extends JpaRepository<UserFile, Long> {
    @Query("SELECT m FROM UserFile m WHERE m.id IN :fileIds")
    List<UserFile> findByIds(@Param("fileIds") List<Long> fileIds);
}
