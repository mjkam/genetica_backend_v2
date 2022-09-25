package com.example.demo;

import com.example.demo.entity.Pipeline;
import com.example.demo.entity.Tool;
import com.example.demo.entity.UserFile;
import com.example.demo.enums.PipelineTaskInputType;
import com.example.demo.repository.PipelineRepository;
import com.example.demo.repository.ToolRepository;
import com.example.demo.repository.UserFileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class FullPipelineTests {
    @Autowired
    private UserFileRepository userFileRepository;
    @Autowired
    private ToolRepository toolRepository;
    @Autowired
    private PipelineRepository pipelineRepository;

    @Test
    void test1() throws Exception {
        Tool unTarTool = toolRepository.save(ToolFactory.unTar());
        Tool bwaMemTool = toolRepository.save(ToolFactory.bwaMem());
        Tool samtoolsView = toolRepository.save(ToolFactory.samToolsView());
        Tool samToolsSort = toolRepository.save(ToolFactory.samToolsSort());
        Tool samToolsIndex = toolRepository.save(ToolFactory.samToolsIndex());

        UserFile referenceFile = userFileRepository.save(UserFileFactory.userFile("human_g1k_v37_decoy.fasta.tar", "human_g1k_v37_decoy.fasta.tar", ""));
        UserFile read_1 = userFileRepository.save(UserFileFactory.userFile("ERR101899_1.fastq.gz", "ERR101899_1.fastq.gz", "ERR101899"));
        UserFile read_2 = userFileRepository.save(UserFileFactory.userFile("ERR101899_2.fastq.gz", "ERR101899_2.fastq.gz", "ERR101899"));

        userFileRepository.save(referenceFile);
        userFileRepository.save(read_1);
        userFileRepository.save(read_2);

        List<Pipeline.Task> pipelineTasks = new ArrayList<>();


        // untar
        List<Pipeline.Task.Input> task1Inputs = new ArrayList<>();
        Pipeline.Task.Input task1Input = new Pipeline.Task.Input(
                PipelineTaskInputType.PIPELINE_INPUT,
                "REFERENCE_TAR",
                0,
                "");
        task1Inputs.add(task1Input);

        Pipeline.Task pipelineTask1 = new Pipeline.Task(1, unTarTool.getId(), task1Inputs);
        pipelineTasks.add(pipelineTask1);

        // bwa-mem
        List<Pipeline.Task.Input> task2Inputs = new ArrayList<>();
        Pipeline.Task.Input task2Input1 = new Pipeline.Task.Input(
                PipelineTaskInputType.TOOL_OUTPUT,
                "REFERENCE_FASTA",
                1,
                "REFERENCE_FASTA");
        Pipeline.Task.Input task2Input2 = new Pipeline.Task.Input(
                PipelineTaskInputType.PIPELINE_INPUT,
                "SAMPLE_READ_1",
                0,
                "");
        Pipeline.Task.Input task2Input3 = new Pipeline.Task.Input(
                PipelineTaskInputType.PIPELINE_INPUT,
                "SAMPLE_READ_2",
                0,
                "");
        task2Inputs.add(task2Input1);
        task2Inputs.add(task2Input2);
        task2Inputs.add(task2Input3);

        Pipeline.Task pipelineTask2 = new Pipeline.Task(2, bwaMemTool.getId(), task2Inputs);
        pipelineTasks.add(pipelineTask2);

        // samtools-view
        List<Pipeline.Task.Input> task3Inputs = new ArrayList<>();
        Pipeline.Task.Input task3Input1 = new Pipeline.Task.Input(
                PipelineTaskInputType.TOOL_OUTPUT,
                "SAM_FILE",
                2,
                "SAMPLE_SAM");
        task3Inputs.add(task3Input1);

        Pipeline.Task pipelineTask3 = new Pipeline.Task(3, samtoolsView.getId(), task3Inputs);
        pipelineTasks.add(pipelineTask3);

        // samtools-sort
        List<Pipeline.Task.Input> task4Inputs = new ArrayList<>();
        Pipeline.Task.Input task4Input1 = new Pipeline.Task.Input(
                PipelineTaskInputType.TOOL_OUTPUT,
                "SAMPLE_BAM",
                3,
                "BAM_FILE");
        task4Inputs.add(task4Input1);

        Pipeline.Task pipelineTask4 = new Pipeline.Task(4, samToolsSort.getId(), task4Inputs);
        pipelineTasks.add(pipelineTask4);

        // samtools index
        List<Pipeline.Task.Input> task5Inputs = new ArrayList<>();
        Pipeline.Task.Input task5Input1 = new Pipeline.Task.Input(
                PipelineTaskInputType.TOOL_OUTPUT,
                "SORTED_BAM",
                4,
                "SORTED_BAM");
        task5Inputs.add(task5Input1);

        Pipeline.Task pipelineTask5 = new Pipeline.Task(5, samToolsIndex.getId(), task5Inputs);
        pipelineTasks.add(pipelineTask5);



        // Pipeline
        List<Pipeline.Input> pipelineInputs = new ArrayList<>();
        List<Pipeline.Output> pipelineOutputs = new ArrayList<>();

        Pipeline.Input pipelineInput1 = new Pipeline.Input(pipelineTask1.getId(), "REFERENCE_TAR", false);
        Pipeline.Input pipelineInput2 = new Pipeline.Input(pipelineTask2.getId(), "SAMPLE_READ_1", true);
        Pipeline.Input pipelineInput3 = new Pipeline.Input(pipelineTask2.getId(), "SAMPLE_READ_2", true);
        Pipeline.Output pipelineOutput = new Pipeline.Output(pipelineTask5.getId(), "BAI_FILE", true);

        pipelineInputs.add(pipelineInput1);
        pipelineInputs.add(pipelineInput2);
        pipelineInputs.add(pipelineInput3);
        pipelineOutputs.add(pipelineOutput);

        Pipeline pipeline = new Pipeline(pipelineInputs, pipelineOutputs, pipelineTasks);
        pipelineRepository.save(pipeline);
    }
}
