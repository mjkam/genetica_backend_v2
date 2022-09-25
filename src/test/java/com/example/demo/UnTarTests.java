package com.example.demo;

import com.example.demo.entity.Pipeline;
import com.example.demo.entity.Tool;
import com.example.demo.entity.UserFile;
import com.example.demo.enums.PipelineTaskInputType;
import com.example.demo.repository.PipelineRepository;
import com.example.demo.repository.ToolRepository;
import com.example.demo.repository.UserFileRepository;
import com.example.demo.service.PipelineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UnTarTests {
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
        Tool unTarTool = toolRepository.save(ToolFactory.unTar());

        UserFile userFile = userFileRepository.save(UserFileFactory.userFile("human_g1k_v37_decoy.fasta.tar", "human_g1k_v37_decoy.fasta.tar", ""));

        List<Pipeline.Task> pipelineTasks = new ArrayList<>();
        List<Pipeline.Task.Input> pipelineTaskInputs = new ArrayList<>();

        Pipeline.Task.Input pipelineTaskInput = new Pipeline.Task.Input(
                PipelineTaskInputType.PIPELINE_INPUT,
                "REFERENCE_TAR",
                0,
                "");
        pipelineTaskInputs.add(pipelineTaskInput);

        Pipeline.Task pipelineTask = new Pipeline.Task(1, unTarTool.getId(), pipelineTaskInputs);
        pipelineTasks.add(pipelineTask);

        List<Pipeline.Input> pipelineInputs = new ArrayList<>();
        List<Pipeline.Output> pipelineOutputs = new ArrayList<>();

        Pipeline.Input pipelineInput = new Pipeline.Input(pipelineTask.getId(), "REFERENCE_TAR", false);
        Pipeline.Output pipelineOutput = new Pipeline.Output(pipelineTask.getId(), "REFERENCE_FASTA", false);

        pipelineInputs.add(pipelineInput);
        pipelineOutputs.add(pipelineOutput);

        Pipeline pipeline = new Pipeline(pipelineInputs, pipelineOutputs, pipelineTasks);
        pipelineRepository.save(pipeline);
    }
}
