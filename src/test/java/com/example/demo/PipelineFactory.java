package com.example.demo;

import com.example.demo.entity.Pipeline;
import com.example.demo.entity.Tool;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

public class PipelineFactory {
    public static Pipeline wesPipeline() {
        Pipeline pipeline = new Pipeline();

        Tool tool1 = ToolFactory.unTar(1L);
        Tool tool2 = ToolFactory.bwaMem(2L);
        Tool tool3 = ToolFactory.samToolsView(3L);

        Pipeline.Task task1 = task(1, tool1.getId());
        Pipeline.Task task2 = task(2, tool2.getId());
        Pipeline.Task task3 = task(3, tool3.getId());

        Pipeline.InputFile inputFile1 = inputFile(1, task1.getId(), 1);
        Pipeline.InputFile inputFile2 = inputFile(2, task2.getId(), 2);
        Pipeline.InputFile inputFile3 = inputFile(3, task2.getId(), 3);

        Pipeline.OutputFile outputFile1 = outputFile(1, task3.getId(), 1);

        Pipeline.InputEdge inputEdge1 = inputEdge(1, 1, 1, 1);
        Pipeline.InputEdge inputEdge2 = inputEdge(2, 2, 2, 2);
        Pipeline.InputEdge inputEdge3 = inputEdge(3, 3, 2, 3);

        Pipeline.ConnectEdge connectEdge1 = connectEdge(1, 1, 1, 2, 1);
        Pipeline.ConnectEdge connectEdge2 = connectEdge(2, 2, 1, 3, 1);

        Pipeline.OutputEdge outputEdge1 = outputEdge(1, 3, 1, 1);

        ReflectionTestUtils.setField(pipeline, "id", 1L);
        ReflectionTestUtils.setField(pipeline, "name", "Whole Exome Sequencing - BWA + GATK 4.0");
        ReflectionTestUtils.setField(pipeline, "tasks", Arrays.asList(task1, task2, task3));
        ReflectionTestUtils.setField(pipeline, "inputFiles", Arrays.asList(inputFile1, inputFile2, inputFile3));
        ReflectionTestUtils.setField(pipeline, "outputFiles", List.of(outputFile1));
        ReflectionTestUtils.setField(pipeline, "inputEdges", Arrays.asList(inputEdge1, inputEdge2, inputEdge3));
        ReflectionTestUtils.setField(pipeline, "connectEdges", Arrays.asList(connectEdge1, connectEdge2));
        ReflectionTestUtils.setField(pipeline, "outputEdges", List.of(outputEdge1));

        return pipeline;
    }

    private static Pipeline.Task task(int id, long toolId) {
        Pipeline.Task task = new Pipeline.Task();
        ReflectionTestUtils.setField(task, "id", id);
        ReflectionTestUtils.setField(task, "toolId", toolId);
        ReflectionTestUtils.setField(task, "posX", 100);
        ReflectionTestUtils.setField(task, "posY", 100);
        return task;
    }

    private static Pipeline.InputFile inputFile(int id, int taskId, int portId) {
        Pipeline.InputFile inputFile = new Pipeline.InputFile();
        ReflectionTestUtils.setField(inputFile, "id", id);
        ReflectionTestUtils.setField(inputFile, "taskId", taskId);
        ReflectionTestUtils.setField(inputFile, "portId", portId);
        ReflectionTestUtils.setField(inputFile, "posX", 100);
        ReflectionTestUtils.setField(inputFile, "posY", 100);
        return inputFile;
    }

    private static Pipeline.OutputFile outputFile(int id, int taskId, int portId) {
        Pipeline.OutputFile outputFile = new Pipeline.OutputFile();
        ReflectionTestUtils.setField(outputFile, "id", id);
        ReflectionTestUtils.setField(outputFile, "taskId", taskId);
        ReflectionTestUtils.setField(outputFile, "portId", portId);
        ReflectionTestUtils.setField(outputFile, "posX", 100);
        ReflectionTestUtils.setField(outputFile, "posY", 100);
        return outputFile;
    }

    private static Pipeline.InputEdge inputEdge(int id, int sourceFileId, int desTaskId, int desPortId) {
        Pipeline.InputEdge inputEdge = new Pipeline.InputEdge();
        ReflectionTestUtils.setField(inputEdge, "id", id);
        ReflectionTestUtils.setField(inputEdge, "sourceFileId", sourceFileId);
        ReflectionTestUtils.setField(inputEdge, "desTaskId", desTaskId);
        ReflectionTestUtils.setField(inputEdge, "desPortId", desPortId);
        return inputEdge;
    }

    private static Pipeline.ConnectEdge connectEdge(int id, int sourceTaskId, int sourceOutputPortId, int desTaskId, int desInputPortId) {
        Pipeline.ConnectEdge connectEdge = new Pipeline.ConnectEdge();
        ReflectionTestUtils.setField(connectEdge, "id", id);
        ReflectionTestUtils.setField(connectEdge, "sourceTaskId", sourceTaskId);
        ReflectionTestUtils.setField(connectEdge, "sourceOutputPortId", sourceOutputPortId);
        ReflectionTestUtils.setField(connectEdge, "desTaskId", desTaskId);
        ReflectionTestUtils.setField(connectEdge, "desInputPortId", desInputPortId);
        return connectEdge;
    }

    private static Pipeline.OutputEdge outputEdge(int id, int sourceTaskId, int sourcePortId, int desFileId) {
        Pipeline.OutputEdge outputEdge = new Pipeline.OutputEdge();
        ReflectionTestUtils.setField(outputEdge, "id", id);
        ReflectionTestUtils.setField(outputEdge, "sourceTaskId", sourceTaskId);
        ReflectionTestUtils.setField(outputEdge, "sourcePortId", sourcePortId);
        ReflectionTestUtils.setField(outputEdge, "desFileId", desFileId);
        return outputEdge;
    }
}
