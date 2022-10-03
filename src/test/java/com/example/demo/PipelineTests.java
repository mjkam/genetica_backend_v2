//package com.example.demo;
//
//import com.example.demo.entity.Pipeline;
//import com.example.demo.entity.UserFile;
//import com.example.demo.service.PipelineInputUserFile;
//import com.example.demo.service.PipelineService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///*
//        1. PipelineJob 생성
//        2. 첫번째 taskJob 생성
//        3. taskJob에 필요한 KubeJob 생성
//        4. 실행
//         */
//
//
//public class PipelineTests {
//    private PipelineService sut;
//
//    @BeforeEach
//    void setup() {
//        this.sut = new PipelineService();
//    }
//
//
//
//    @Test
//    @DisplayName("Pipeline 실행하면 Pipeline 실행정보 반환")
//    void test1() {
//        //given
////        UserFile referenceFile = UserFileFactory.userFileWithId(1L, "human_g1k_v37_decoy.fasta.tar", "human_g1k_v37_decoy.fasta.tar", "")
////        UserFile read_1 = UserFileFactory.userFileWithId(2L, "ERR101899_1.fastq.gz", "ERR101899_1.fastq.gz", "ERR101899")
////        UserFile read_2 = UserFileFactory.userFileWithId(3L, "ERR101899_1.fastq.gz", "ERR101899_1.fastq.gz", "ERR101899")
////        PipelineInputUserFile input1 = pipelineInputUserFile(1, referenceFile.getId());
////        PipelineInputUserFile input2 = pipelineInputUserFile(2, read_1.getId());
////        PipelineInputUserFile input3 = pipelineInputUserFile(3, read_2.getId());
////        List<PipelineInputUserFile> pipelineInputUserFiles = Arrays.asList(input1, input2, input3);
//        PipelineInputUserFile input1 = pipelineInputUserFile(1, 1L);
//        PipelineInputUserFile input2 = pipelineInputUserFile(2, 2L);
//        PipelineInputUserFile input3 = pipelineInputUserFile(3, 3L);
//        List<PipelineInputUserFile> pipelineInputUserFiles = Arrays.asList(input1, input2, input3);
//        long pipelineId = 1L;
//
//        //when
//        this.sut.executePipeline(pipelineId, pipelineInputUserFiles);
//
//        //then
////        assertThat(result).isEqualTo(1L);
//    }
//
//    private PipelineInputUserFile pipelineInputUserFile(int inputFileId, long userFileId) {
//        PipelineInputUserFile pipelineInputUserFile = new PipelineInputUserFile();
//        ReflectionTestUtils.setField(pipelineInputUserFile, "inputFileId", inputFileId);
//        ReflectionTestUtils.setField(pipelineInputUserFile, "userFileId", userFileId);
//
//        return pipelineInputUserFile;
//    }
//}
