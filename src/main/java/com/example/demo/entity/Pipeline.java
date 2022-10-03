package com.example.demo.entity;

import com.example.demo.converter.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pipeline")
@NoArgsConstructor
@Getter
public class Pipeline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tasks")
    @Convert(converter = PipelineTaskConverter.class)
    private List<Task> tasks;

    @Column(name = "input_files")
    @Convert(converter = PipelineInputFileConverter.class)
    private List<InputFile> inputFiles;

    @Column(name = "output_files")
    @Convert(converter = PipelineOutputFileConverter.class)
    private List<OutputFile> outputFiles;

    @Column(name = "input_edges")
    @Convert(converter = PipelineInputEdgeConverter.class)
    private List<InputEdge> inputEdges;

    @Column(name = "connect_edges")
    @Convert(converter = PipelineConnectEdgeConverter.class)
    private List<ConnectEdge> connectEdges;

    @Column(name = "output_edges")
    @Convert(converter = PipelineOutputEdgeConverter.class)
    private List<OutputEdge> outputEdges;

    public boolean isOutput(int taskId, int portId) {
        return outputEdges.stream()
                .anyMatch(o -> o.sourceTaskId.equals(taskId) && o.sourcePortId.equals(portId));
    }


    public Task findExecutableTask(List<Integer> finishedTaskIds) {
        return this.tasks.stream()
                .filter(o -> !finishedTaskIds.contains(o.id))
                .filter(
                        o -> this.connectEdges.stream()
                                .filter(e -> e.desTaskId.equals(o.id))
                                .allMatch(e -> finishedTaskIds.contains(e.sourceTaskId)))
                .findFirst()
                .orElseThrow(() -> {
                    finishedTaskIds.forEach(System.out::println);
                    throw new RuntimeException("Next Task Not Found");
                });
    }

    public InputFile getInputFile(int inputFileId) {
        return inputFiles.stream()
                .filter(o -> o.getId().equals(inputFileId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("not match file: " + inputFileId));
    }

    public long getToolId(int taskId) {
        Task task = this.tasks.stream()
                .filter(o -> o.getId().equals(taskId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Task Not Found: id: " + taskId));
        return task.getToolId();
    }

    public ConnectEdge getTask(Integer pipelineTaskId, Integer portId) {
        return this.connectEdges.stream()
                .filter(o -> o.desTaskId.equals(pipelineTaskId))
                .filter(o -> o.desInputPortId.equals(portId))
                .findAny()
                .orElseThrow(() -> new RuntimeException(pipelineTaskId + " " + portId));
    }



    @NoArgsConstructor
    @Getter
    public static class Task {
        private Integer id;
        private Long toolId;
        private Integer posX;
        private Integer posY;
    }

    @NoArgsConstructor
    @Getter
    public static class InputFile {
        private Integer id;
        private Integer taskId;
        private Integer portId;
        private Integer posX;
        private Integer posY;
    }

    @NoArgsConstructor
    @Getter
    public static class OutputFile {
        private Integer id;
        private Integer taskId;
        private Integer portId;
        private Integer posX;
        private Integer posY;
    }

    @NoArgsConstructor
    @Getter
    public static class InputEdge {
        private Integer id;
        private Integer sourceFileId;
        private Integer desTaskId;
        private Integer desPortId;
    }

    @NoArgsConstructor
    @Getter
    public static class ConnectEdge {
        private Integer id;
        private Integer sourceTaskId;
        private Integer sourceOutputPortId;
        private Integer desTaskId;
        private Integer desInputPortId;
    }

    @NoArgsConstructor
    @Getter
    public static class OutputEdge {
        private Integer id;
        private Integer sourceTaskId;
        private Integer sourcePortId;
        private Integer desFileId;
    }

}
