package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "sample_id")
    private String sampleId;

    public UserFile(String name, String s3Name, String sampleId) {
        this.name = name;
        this.s3Name = s3Name;
        this.sampleId = sampleId;
    }
}
