package com.example.demo.controller.dto;

import com.example.demo.converter.ToolInputPortConverter;
import com.example.demo.converter.ToolOutputPortConverter;
import com.example.demo.entity.Tool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class GetToolsResponse {
    private List<ToolDto> tools;

    public GetToolsResponse(List<Tool> tools) {
        this.tools = tools.stream()
                .map(ToolDto::new)
                .collect(Collectors.toList());
    }

    @NoArgsConstructor
    @Getter
    static class ToolDto {
        private Long id;
        private String name;
        private String command;
        private List<InputPort> inputPorts;
        private List<OutputPort>  outputPorts;

        public ToolDto(Tool tool) {
            this.id = tool.getId();
            this.name = tool.getName();
            this.command = tool.getCommand();
            this.inputPorts = tool.getInputPorts().stream()
                    .map(InputPort::new)
                    .collect(Collectors.toList());
            this.outputPorts = tool.getOutputPorts().stream()
                    .map(OutputPort::new)
                    .collect(Collectors.toList());
        }
    }

    @NoArgsConstructor
    @Getter
    static class InputPort {
        private Integer id;
        private String label;
        private String varName;
        private Integer posX;
        private Integer posY;

        public InputPort(Tool.InputPort inputPort) {
            this.id = inputPort.getId();
            this.label = inputPort.getLabel();
            this.varName = inputPort.getVarName();
            this.posX = inputPort.getPosX();
            this.posY = inputPort.getPosY();
        }
    }

    @NoArgsConstructor
    @Getter
    static class OutputPort {
        private Integer id;
        private String label;
        private String outputName;
        private Integer posX;
        private Integer posY;

        public OutputPort(Tool.OutputPort outputPort) {
            this.id = outputPort.getId();
            this.label = outputPort.getLabel();
            this.outputName = outputPort.getOutputName();;
            this.posX = outputPort.getPosX();
            this.posY = outputPort.getPosY();
        }
    }
}