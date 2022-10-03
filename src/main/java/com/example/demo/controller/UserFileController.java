package com.example.demo.controller;

import com.example.demo.controller.dto.GetUserFilesResponse;
import com.example.demo.service.UserFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserFileController {
    private final UserFileService userFileService;

    @GetMapping("/files")
    public ResponseEntity<GetUserFilesResponse> userFiles() {
        return ResponseEntity.ok(new GetUserFilesResponse(userFileService.findUserFiles()));
    }
}
