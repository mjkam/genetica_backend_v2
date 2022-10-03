package com.example.demo.controller;

import com.example.demo.controller.dto.GetPipelinesResponse;
import com.example.demo.controller.dto.RunPipelineRequest;
import com.example.demo.service.PipelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PipelineController {
    private final PipelineService pipelineService;

    @PostMapping("/pipeline/run")
    public ResponseEntity<Object> runPipeline(@RequestBody RunPipelineRequest request) throws IOException, InterruptedException {
        pipelineService.executePipeline(request.getPipelineId(), request.toInputUserFiles());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pipelines")
    public ResponseEntity<GetPipelinesResponse> getPipelines() {
        return ResponseEntity.ok(new GetPipelinesResponse(pipelineService.getPipeline()));
    }




}
