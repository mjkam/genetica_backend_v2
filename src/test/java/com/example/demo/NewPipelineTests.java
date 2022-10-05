package com.example.demo;

import com.example.demo.entity.Pipeline;
import com.example.demo.entity.Tool;
import com.example.demo.entity.UserFile;
import com.example.demo.enums.JobStatus;
import com.example.demo.repository.PipelineRepository;
import com.example.demo.repository.ToolRepository;
import com.example.demo.repository.UserFileRepository;
import com.example.demo.service.PipelineInputUserFile;
import com.example.demo.service.PipelineResultService;
import com.example.demo.service.PipelineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;

@SpringBootTest
public class NewPipelineTests {
    @Autowired
    private PipelineRepository pipelineRepository;
    @Autowired
    private ToolRepository toolRepository;
    @Autowired
    private UserFileRepository userFileRepository;

    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private PipelineResultService pipelineResultService;

    @Test
    void test() throws IOException, InterruptedException {
        UserFile referenceFile = userFileRepository.save(UserFileFactory.userFileWithoutId("human_g1k_v37_decoy.fasta.tar", 10293018, "human_g1k_v37_decoy.fasta.tar", ""));
        UserFile read_1 = userFileRepository.save(UserFileFactory.userFileWithoutId("ERR101899_1.fastq.gz", 29184, "ERR101899_1.fastq.gz", "ERR101899"));
        UserFile read_2 = userFileRepository.save(UserFileFactory.userFileWithoutId("ERR101899_2.fastq.gz", 110293058, "ERR101899_2.fastq.gz", "ERR101899"));

        Tool tool1 = toolRepository.save(ToolFactory.unTar(1L));
        Tool tool2 = toolRepository.save(ToolFactory.bwaMem(2L));
        Tool tool3 = toolRepository.save(ToolFactory.samToolsView(3L));

//        Pipeline pipeline = pipelineRepository.save(PipelineFactory.wesPipeline());
//
//
//        PipelineInputUserFile input1 = pipelineInputUserFile(1, referenceFile.getId());
//        PipelineInputUserFile input2 = pipelineInputUserFile(2, read_1.getId());
//        PipelineInputUserFile input3 = pipelineInputUserFile(3, read_2.getId());


//        pipelineService.executePipeline(1L, Arrays.asList(input1, input2, input3));
//        pipelineResultService.handleKubeJobResult(1L, 1, 3L, JobStatus.SUCCESS);
//        pipelineResultService.handleKubeJobResult(1L, 1, 5L, JobStatus.SUCCESS);
//        pipelineResultService.handleKubeJobResult(1L, 2, 8L, JobStatus.SUCCESS);

    }

    private PipelineInputUserFile pipelineInputUserFile(int inputFileId, long fileId) {
        PipelineInputUserFile pipelineInputUserFile = new PipelineInputUserFile();
        ReflectionTestUtils.setField(pipelineInputUserFile, "inputFileId", inputFileId);
        ReflectionTestUtils.setField(pipelineInputUserFile, "userFileId", fileId);
        return pipelineInputUserFile;
    }
}
