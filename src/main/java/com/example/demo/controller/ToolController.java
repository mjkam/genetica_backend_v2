package com.example.demo.controller;

import com.example.demo.controller.dto.GetToolsResponse;
import com.example.demo.entity.Tool;
import com.example.demo.service.ToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ToolController {
    private final ToolService toolService;

    @GetMapping("/tools")
    public ResponseEntity<GetToolsResponse> tools() {
        List<Tool> totalTools = toolService.findTotalTools();
        return ResponseEntity.ok(new GetToolsResponse(totalTools));
    }
}
