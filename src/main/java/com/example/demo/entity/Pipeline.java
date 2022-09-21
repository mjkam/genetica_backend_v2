package com.example.demo.entity;

import com.example.demo.converter.PipelineInputConverter;
import com.example.demo.converter.PipelineOutputConverter;
import com.example.demo.converter.PipelineTaskConverter;
import com.example.demo.enums.PipelineTaskInputType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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

    @Column(name = "inputs")
    @Convert(converter = PipelineInputConverter.class)
    private List<Input> inputs;

    @Column(name = "outputs")
    @Convert(converter = PipelineOutputConverter.class)
    private List<Output> outputs;

    @Column(name = "tasks")
    @Convert(converter = PipelineTaskConverter.class)
    private List<Task> tasks;

    public Pipeline(List<Pipeline.Input> inputs, List<Pipeline.Output> outputs, List<Pipeline.Task> pipelineTasks) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.tasks = pipelineTasks;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Input {
        private Integer pipelineTaskId;
        private String pipelineTaskLabel;
        private boolean isSample;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Output {
        private Integer pipelineTaskId;
        private String pipelineTaskLabel;
        private boolean isSample;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Task {
        private Integer id;
        private Long toolId;
        private List<Task.Input> inputs;

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        public static class Input {
            private PipelineTaskInputType type;
            private String toolLabel;
            private Integer sourcePipelineTaskId;
            private String sourcePipelineTaskLabel;
        }
    }
}