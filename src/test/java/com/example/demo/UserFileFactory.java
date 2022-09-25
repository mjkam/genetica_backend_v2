package com.example.demo;

import com.example.demo.entity.UserFile;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.Column;

public class UserFileFactory {
    public static UserFile userFile(String name, String s3Name, String sampleId) {
        UserFile userFile = new UserFile();
        ReflectionTestUtils.setField(userFile, "name", name);
        ReflectionTestUtils.setField(userFile, "s3Name", s3Name);
        ReflectionTestUtils.setField(userFile, "sampleId", sampleId);

        return userFile;
    }
}
