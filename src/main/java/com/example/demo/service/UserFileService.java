package com.example.demo.service;

import com.example.demo.entity.UserFile;
import com.example.demo.repository.UserFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserFileService {
    private final UserFileRepository userFileRepository;
    private final AwsS3 awsS3;

    public List<UserFile> findUserFiles() {
        return userFileRepository.findAll();
    }

    public Long deleteFile(long fileId) {
        UserFile userFile = userFileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("File. id: " + fileId));
        awsS3.remove(userFile.getS3Name(), "/");
        userFileRepository.delete(userFile);
        return userFile.getId();
    }

    public void uploadFile(List<MultipartFile> files, String sampleId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<UserFile> newUserFiles = new ArrayList<>();
        for (MultipartFile multipartFile: files) {
            String s3Name = awsS3.uploadFile(multipartFile, "/");
            newUserFiles.add(new UserFile(multipartFile.getOriginalFilename(), s3Name, multipartFile.getSize(), "/", sampleId, localDateTime));
        }
        userFileRepository.saveAll(newUserFiles);
    }
}
