package com.example.demo.controller.dto;

import com.example.demo.entity.Pipeline;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class GetPipelinesResponse {
    private List<PipelineDto> pipelines;

    public GetPipelinesResponse(List<Pipeline> pipelines) {
        this.pipelines = pipelines.stream()
                .map(PipelineDto::new)
                .collect(Collectors.toList());;
    }

    @NoArgsConstructor
    @Getter
    static class PipelineDto {
        private Long id;
        private String name;
        private List<TaskDto> tasks;
        private List<InputFileDto> inputFiles;
        private List<OutputFileDto> outputFiles;
        private List<InputEdgeDto> inputEdges;
        private List<ConnectEdgeDto> connectEdges;
        private List<OutputEdgeDto> outputEdges;

        public PipelineDto(Pipeline pipeline) {
            this.id = pipeline.getId();
            this.name = pipeline.getName();
            this.tasks = pipeline.getTasks().stream()
                    .map(TaskDto::new)
                    .collect(Collectors.toList());
            this.inputFiles = pipeline.getInputFiles().stream()
                    .map(InputFileDto::new)
                    .collect(Collectors.toList());
            this.outputFiles = pipeline.getOutputFiles().stream()
                    .map(OutputFileDto::new)
                    .collect(Collectors.toList());
            this.inputEdges = pipeline.getInputEdges().stream()
                    .map(InputEdgeDto::new)
                    .collect(Collectors.toList());
            this.connectEdges = pipeline.getConnectEdges().stream()
                    .map(ConnectEdgeDto::new)
                    .collect(Collectors.toList());
            this.outputEdges = pipeline.getOutputEdges().stream()
                    .map(OutputEdgeDto::new)
                    .collect(Collectors.toList());
        }
    }

    @NoArgsConstructor
    @Getter
    public static class TaskDto {
        private Integer id;
        private Long toolId;
        private Integer posX;
        private Integer posY;

        public TaskDto(Pipeline.Task task) {
            this.id = task.getId();
            this.toolId = task.getToolId();
            this.posX = task.getPosX();
            this.posY = task.getPosY();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class InputFileDto {
        private Integer id;
        private Integer taskId;
        private Integer portId;
        private Integer posX;
        private Integer posY;

        public InputFileDto(Pipeline.InputFile inputFile) {
            this.id = inputFile.getId();
            this.taskId = inputFile.getTaskId();
            this.portId = inputFile.getPortId();
            this.posX = inputFile.getPosX();
            this.posY = inputFile.getPosY();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class OutputFileDto {
        private Integer id;
        private Integer taskId;
        private Integer portId;
        private Integer posX;
        private Integer posY;

        public OutputFileDto(Pipeline.OutputFile outputFile) {
            this.id = outputFile.getId();
            this.taskId = outputFile.getTaskId();
            this.portId = outputFile.getPortId();
            this.posX = outputFile.getPosX();
            this.posY = outputFile.getPosY();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class InputEdgeDto {
        private Integer id;
        private Integer sourceFileId;
        private Integer desTaskId;
        private Integer desPortId;

        public InputEdgeDto(Pipeline.InputEdge inputEdge) {
            this.id = inputEdge.getId();
            this.sourceFileId = inputEdge.getSourceFileId();
            this.desTaskId = inputEdge.getDesTaskId();
            this.desPortId = inputEdge.getDesPortId();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class ConnectEdgeDto {
        private Integer id;
        private Integer sourceTaskId;
        private Integer sourceOutputPortId;
        private Integer desTaskId;
        private Integer desInputPortId;

        public ConnectEdgeDto(Pipeline.ConnectEdge connectEdge) {
            this.id = connectEdge.getId();
            this.sourceTaskId = connectEdge.getSourceTaskId();
            this.sourceOutputPortId = connectEdge.getSourceOutputPortId();
            this.desTaskId = connectEdge.getDesTaskId();
            this.desInputPortId = connectEdge.getDesInputPortId();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class OutputEdgeDto {
        private Integer id;
        private Integer sourceTaskId;
        private Integer sourcePortId;
        private Integer desFileId;

        public OutputEdgeDto(Pipeline.OutputEdge outputEdge) {
            this.id = outputEdge.getId();
            this.sourceTaskId = outputEdge.getSourceTaskId();
            this.sourcePortId = outputEdge.getSourcePortId();
            this.desFileId = outputEdge.getDesFileId();
        }
    }
}