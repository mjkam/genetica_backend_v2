package com.example.demo;

import com.example.demo.controller.dto.RunPipelineRequest;
import com.example.demo.entity.Pipeline;
import com.example.demo.entity.Tool;
import com.example.demo.entity.UserFile;
import com.example.demo.enums.PipelineTaskInputType;
import com.example.demo.repository.PipelineRepository;
import com.example.demo.repository.ToolRepository;
import com.example.demo.repository.UserFileRepository;
import com.example.demo.service.PipelineJobInputUserFile;
import com.example.demo.service.PipelineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class MemToSamTests {
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private UserFileRepository userFileRepository;
    @Autowired
    private ToolRepository toolRepository;
    @Autowired
    private PipelineRepository pipelineRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void test1() throws Exception {
        List<Tool.Input> toolInputs = new ArrayList<>();
        List<Tool.Output> toolOutputs = new ArrayList<>();

        Tool.Input toolInput = new Tool.Input("INPUT_REFERENCE", false);
        Tool.Output toolOutput = new Tool.Output("OUTPUT_REFERENCE", "${INPUT_REFERENCE}", false);

        toolInputs.add(toolInput);
        toolOutputs.add(toolOutput);

        Tool tool = new Tool("bwa", toolInputs, toolOutputs, "bwa index ${INPUT_REFERENCE}", "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/bwa:latest");
        tool = toolRepository.save(tool);

        UserFile userFile = new UserFile();
        ReflectionTestUtils.setField(userFile, "name", "MT.fa");
        ReflectionTestUtils.setField(userFile, "s3Name", "MT.fa");
        ReflectionTestUtils.setField(userFile, "sampleId", "");

        userFileRepository.save(userFile);

        List<Pipeline.Task> pipelineTasks = new ArrayList<>();
        List<Pipeline.Task.Input> pipelineTaskInputs = new ArrayList<>();

        Pipeline.Task.Input pipelineTaskInput = new Pipeline.Task.Input(
                PipelineTaskInputType.PIPELINE_INPUT,
                "INPUT_REFERENCE",
                0,
                "");
        pipelineTaskInputs.add(pipelineTaskInput);

        Pipeline.Task pipelineTask = new Pipeline.Task(1, tool.getId(), pipelineTaskInputs);
        pipelineTasks.add(pipelineTask);

        List<Pipeline.Input> pipelineInputs = new ArrayList<>();
        List<Pipeline.Output> pipelineOutputs = new ArrayList<>();

        Pipeline.Input pipelineInput = new Pipeline.Input(pipelineTask.getId(), "INPUT_REFERENCE", false);
        Pipeline.Output pipelineOutput = new Pipeline.Output(pipelineTask.getId(), "OUTPUT_REFERENCE", false);

        pipelineInputs.add(pipelineInput);
        pipelineOutputs.add(pipelineOutput);

        Pipeline pipeline = new Pipeline(pipelineInputs, pipelineOutputs, pipelineTasks);
        pipelineRepository.save(pipeline);


        RunPipelineRequest.RequestFile requestFile = new RunPipelineRequest.RequestFile();
        ReflectionTestUtils.setField(requestFile, "taskId", 1);
        ReflectionTestUtils.setField(requestFile, "taskInputLabel", "INPUT_REFERENCE");
        ReflectionTestUtils.setField(requestFile, "fileId", 1L);

        RunPipelineRequest request = new RunPipelineRequest();
        ReflectionTestUtils.setField(request, "pipelineId", 1L);
        ReflectionTestUtils.setField(request, "requestFiles", List.of(requestFile));

        List<PipelineJobInputUserFile> pipelineJobInputUserFiles = new ArrayList<>();
        pipelineJobInputUserFiles.add(new PipelineJobInputUserFile(1, "INPUT_REFERENCE", userFile.getId()));

//        pipelineService.runPipeline(pipeline.getId(), pipelineJobInputUserFiles);
    }
}
