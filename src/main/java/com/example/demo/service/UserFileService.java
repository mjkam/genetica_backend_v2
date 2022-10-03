package com.example.demo.service;

import com.example.demo.entity.UserFile;
import com.example.demo.repository.UserFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFileService {
    private final UserFileRepository userFileRepository;

    public List<UserFile> findUserFiles() {
        return userFileRepository.findAll();
    }
}
