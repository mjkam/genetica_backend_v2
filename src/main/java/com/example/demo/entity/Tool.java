package com.example.demo.entity;

import com.example.demo.converter.PipelineInputEdgeConverter;
import com.example.demo.converter.PipelineOutputEdgeConverter;
import com.example.demo.converter.ToolInputPortConverter;
import com.example.demo.converter.ToolOutputPortConverter;
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

    @Column(name = "command")
    private String command;

    @Column(name = "image")
    private String image;

    @Column(name = "input_ports")
    @Convert(converter = ToolInputPortConverter.class)
    private List<InputPort> inputPorts;

    @Column(name = "output_ports")
    @Convert(converter = ToolOutputPortConverter.class)
    private List<OutputPort> outputPorts;

    public Tool(String name, List<InputPort> inputPorts, List<OutputPort> outputPorts, String command, String image) {
        this.name = name;
        this.inputPorts = inputPorts;
        this.outputPorts = outputPorts;
        this.command = command;
        this.image = image;
    }

    public String getInputPortVarName(int portId) {
        InputPort inputPort = this.inputPorts.stream()
                .filter(o -> o.getId().equals(portId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("input port not found: " + portId));

        return inputPort.getVarName();
    }

    public String getOutputName(int portId) {
        OutputPort outputPort = this.outputPorts.stream()
                .filter(o -> o.getId().equals(portId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("output port not found: " + portId));

        return outputPort.getOutputName();
    }



    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class InputPort {
        private Integer id;
        private String label;
        private String varName;
        private Integer posX;
        private Integer posY;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class OutputPort {
        private Integer id;
        private String label;
        private String outputName;
        private Integer posX;
        private Integer posY;
    }
}