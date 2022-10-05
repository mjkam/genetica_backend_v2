package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_file")
@NoArgsConstructor
@Getter
public class UserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "s3_name")
    private String s3Name;

    @Column(name = "size")
    private Long size;

    @Column(name = "directory")
    private String directory;

    @Column(name = "sample_id")
    private String sampleId;

    @Column(name = "created_datetime")
    private LocalDateTime createdDateTime;

    public UserFile(String name, String s3Name, long size, String directory, String sampleId, LocalDateTime currentDateTime) {
        this.name = name;
        this.s3Name = s3Name;
        this.size = size;
        this.directory = directory;
        this.sampleId = sampleId;
        this.createdDateTime = currentDateTime;
    }
}
