package com.example.demo.entity;

import com.example.demo.converter.ToolInputConverter;
import com.example.demo.converter.ToolOutputConverter;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "tool")
@NoArgsConstructor
@Getter
public class Tool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "inputs")
    @Convert(converter = ToolInputConverter.class)
    private List<Input> inputs;

    @Column(name = "outputs")
    @Convert(converter = ToolOutputConverter.class)
    private List<Output> outputs;

    @Column(name = "command")
    private String command;

    @Column(name = "image")
    private String image;

    public Tool(String name, List<Input> inputs, List<Output> outputs, String command, String image) {
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.command = command;
        this.image = image;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Input {
        private String label;
        private boolean requireSampleId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Output {
        private String label;
        private boolean saveWithSampleId;
        private String outputName;
    }
}