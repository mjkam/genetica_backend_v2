package com.example.demo.controller;

import com.example.demo.controller.dto.RunPipelineRequest;
import com.example.demo.service.PipelineJobInputUserFile;
import com.example.demo.service.PipelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PipelineController {
    private final PipelineService pipelineService;

    @PostMapping("/pipeline/run")
    public ResponseEntity<Object> runPipeline(@RequestBody RunPipelineRequest request) {
        List<PipelineJobInputUserFile> pipelineJobInputUserFiles = request.getRequestFiles().stream()
                .map(o -> new PipelineJobInputUserFile(o.getTaskId(), o.getTaskInputLabel(), o.getFileId()))
                .collect(Collectors.toList());

        pipelineService.runPipeline(request.getPipelineId(), pipelineJobInputUserFiles);
        return ResponseEntity.ok().build();
    }
}
