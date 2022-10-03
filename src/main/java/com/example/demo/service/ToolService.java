package com.example.demo.service;

import com.example.demo.entity.Tool;
import com.example.demo.repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToolService {
    private final ToolRepository toolRepository;

    public List<Tool> findTotalTools() {
        return toolRepository.findAll();
    }
}
