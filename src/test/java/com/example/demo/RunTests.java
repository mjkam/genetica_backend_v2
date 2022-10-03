//package com.example.demo;
//
//import com.example.demo.controller.dto.KubeJobStatusUpdateRequest;
//import com.example.demo.controller.dto.RunPipelineRequest;
//import com.example.demo.entity.Pipeline;
//import com.example.demo.entity.Tool;
//import com.example.demo.entity.UserFile;
//import com.example.demo.enums.JobStatus;
//import com.example.demo.enums.PipelineTaskInputType;
//import com.example.demo.repository.PipelineRepository;
//import com.example.demo.repository.ToolRepository;
//import com.example.demo.repository.UserFileRepository;
//import com.example.demo.service.PipelineJobInputUserFile;
//import com.example.demo.service.PipelineService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
////@Transactional
//public class RunTests {
//    @Autowired
//    private PipelineService pipelineService;
//    @Autowired
//    private UserFileRepository userFileRepository;
//    @Autowired
//    private ToolRepository toolRepository;
//    @Autowired
//    private PipelineRepository pipelineRepository;
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void test1() throws Exception {
//        List<Tool.Input> toolInputs = new ArrayList<>();
//        List<Tool.Output> toolOutputs = new ArrayList<>();
//
//        Tool.Input toolInput = new Tool.Input("INPUT_REFERENCE", false);
//        Tool.Output toolOutput = new Tool.Output("OUTPUT_REFERENCE", "${INPUT_REFERENCE}", false);
//
//        toolInputs.add(toolInput);
//        toolOutputs.add(toolOutput);
//
//        Tool tool = new Tool("bwa", toolInputs, toolOutputs, "bwa index ${INPUT_REFERENCE}", "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/bwa:latest");
//        tool = toolRepository.save(tool);
//
//        UserFile userFile = new UserFile();
//        ReflectionTestUtils.setField(userFile, "name", "MT.fa");
//        ReflectionTestUtils.setField(userFile, "s3Name", "MT.fa");
//        ReflectionTestUtils.setField(userFile, "sampleId", "");
//
//        userFileRepository.save(userFile);
//
//        List<Pipeline.Task> pipelineTasks = new ArrayList<>();
//        List<Pipeline.Task.Input> pipelineTaskInputs = new ArrayList<>();
//
//        Pipeline.Task.Input pipelineTaskInput = new Pipeline.Task.Input(
//                PipelineTaskInputType.PIPELINE_INPUT,
//                "INPUT_REFERENCE",
//                0,
//                "");
//        pipelineTaskInputs.add(pipelineTaskInput);
//
//        Pipeline.Task pipelineTask = new Pipeline.Task(1, tool.getId(), pipelineTaskInputs);
//        pipelineTasks.add(pipelineTask);
//
//        List<Pipeline.Input> pipelineInputs = new ArrayList<>();
//        List<Pipeline.Output> pipelineOutputs = new ArrayList<>();
//
//        Pipeline.Input pipelineInput = new Pipeline.Input(pipelineTask.getId(), "INPUT_REFERENCE", false);
//        Pipeline.Output pipelineOutput = new Pipeline.Output(pipelineTask.getId(), "OUTPUT_REFERENCE", false);
//
//        pipelineInputs.add(pipelineInput);
//        pipelineOutputs.add(pipelineOutput);
//
//        Pipeline pipeline = new Pipeline(pipelineInputs, pipelineOutputs, pipelineTasks);
//        pipelineRepository.save(pipeline);
//
//
//        RunPipelineRequest.RequestFile requestFile = new RunPipelineRequest.RequestFile();
//        ReflectionTestUtils.setField(requestFile, "taskId", 1);
//        ReflectionTestUtils.setField(requestFile, "taskInputLabel", "INPUT_REFERENCE");
//        ReflectionTestUtils.setField(requestFile, "fileId", 1L);
//
//        RunPipelineRequest request = new RunPipelineRequest();
//        ReflectionTestUtils.setField(request, "pipelineId", 1L);
//        ReflectionTestUtils.setField(request, "requestFiles", List.of(requestFile));
//
//        List<PipelineJobInputUserFile> pipelineJobInputUserFiles = new ArrayList<>();
//        pipelineJobInputUserFiles.add(new PipelineJobInputUserFile(1, "INPUT_REFERENCE", userFile.getId()));
//
////        pipelineService.runPipeline(pipeline.getId(), pipelineJobInputUserFiles);
//    }
//
//    @Test
//    void test2() throws Exception {
//        mockMvc
//                .perform(
//                        post("/result")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(createRequest(1L, 1, 1L, JobStatus.RUNNING)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void test3() throws Exception {
//        mockMvc
//                .perform(
//                        post("/result")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(createRequest(1L, 1, 1L, JobStatus.SUCCESS)))
//                .andExpect(status().isOk());
//    }
//
//    private String createRequest(long pipelineJobId, int pipelineTaskId, long kubeJobId, JobStatus jobStatus) throws JsonProcessingException {
//        KubeJobStatusUpdateRequest request = new KubeJobStatusUpdateRequest();
//        ReflectionTestUtils.setField(request, "pipelineJobId", pipelineJobId);
//        ReflectionTestUtils.setField(request, "pipelineTaskId", pipelineTaskId);
//        ReflectionTestUtils.setField(request, "kubeJobId", kubeJobId);
//        ReflectionTestUtils.setField(request, "jobStatus", jobStatus);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.writeValueAsString(request);
//    }
//}
