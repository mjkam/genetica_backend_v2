package com.example.demo.controller;

import com.example.demo.controller.dto.GetUserFilesResponse;
import com.example.demo.service.AwsS3;
import com.example.demo.service.UserFileService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserFileController {
    private final UserFileService userFileService;

    @GetMapping("/files")
    public ResponseEntity<GetUserFilesResponse> userFiles() {
        return ResponseEntity.ok(new GetUserFilesResponse(userFileService.findUserFiles()));
    }

    @PostMapping("/delete")
    public ResponseEntity<RemoveFileResponse> deleteFile(@RequestBody RemoveFileRequest request) {
        long deletedFileId = userFileService.deleteFile(request.getFileId());
        return ResponseEntity.ok(new RemoveFileResponse(deletedFileId));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam("uploadFiles") List<MultipartFile> files,
            @RequestParam("sampleId") String sampleId) {
        userFileService.uploadFile(files, sampleId);
        return ResponseEntity.ok().build();
    }

    @Data
    static class RemoveFileRequest {
        private Long fileId;
    }

    @Data
    static class RemoveFileResponse {
        private Long fileId;

        public RemoveFileResponse(long fileId) {
            this.fileId = fileId;
        }
    }

    @Data
    static class FileListRequest {
        private String directory;
    }
}
