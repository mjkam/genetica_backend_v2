package com.example.demo.controller.dto;

import com.example.demo.entity.UserFile;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class GetUserFilesResponse {
    private List<UserFileDto> files;

    public GetUserFilesResponse(List<UserFile> userFiles) {
        this.files = userFiles.stream()
                .map(UserFileDto::new)
                .collect(Collectors.toList());
    }

    @NoArgsConstructor
    @Getter
    static class UserFileDto {
        private Long id;
        private String name;
        private Long size;
        private String sampleId;
        private LocalDateTime createdDateTime;

        public UserFileDto(UserFile userFile) {
            this.id = userFile.getId();;
            this.name = userFile.getName();
            this.size = userFile.getSize();
            this.sampleId = userFile.getSampleId();
            this.createdDateTime = userFile.getCreatedDateTime();
        }
    }
}
