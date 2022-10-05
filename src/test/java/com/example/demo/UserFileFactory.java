package com.example.demo;

import com.example.demo.entity.UserFile;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class UserFileFactory {
    public static UserFile userFileWithId(long fileId, String name, long size, String s3Name, String sampleId) {
        UserFile userFile = new UserFile();
        ReflectionTestUtils.setField(userFile, "id", fileId);
        ReflectionTestUtils.setField(userFile, "name", name);
        ReflectionTestUtils.setField(userFile, "s3Name", s3Name);
        ReflectionTestUtils.setField(userFile, "size", size);
        ReflectionTestUtils.setField(userFile, "directory", "/");
        ReflectionTestUtils.setField(userFile, "sampleId", sampleId);
        ReflectionTestUtils.setField(userFile, "createdDateTime", LocalDateTime.now());

        return userFile;
    }

    public static UserFile userFileWithoutId(String name, long size, String s3Name, String sampleId) {
        UserFile userFile = new UserFile();
        ReflectionTestUtils.setField(userFile, "name", name);
        ReflectionTestUtils.setField(userFile, "s3Name", s3Name);
        ReflectionTestUtils.setField(userFile, "size", size);
        ReflectionTestUtils.setField(userFile, "directory", "/");
        ReflectionTestUtils.setField(userFile, "sampleId", sampleId);
        ReflectionTestUtils.setField(userFile, "createdDateTime", LocalDateTime.now());

        return userFile;
    }
}
