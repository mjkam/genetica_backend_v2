package com.example.demo;

import com.example.demo.converter.PipelineInputEdgeConverter;
import com.example.demo.converter.PipelineOutputEdgeConverter;
import com.example.demo.entity.Tool;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.Column;
import javax.persistence.Convert;
import java.util.ArrayList;
import java.util.List;

public class ToolFactory {
    public static Tool unTar(long id) {
        List<Tool.InputPort> inputPorts = new ArrayList<>();
        List<Tool.OutputPort> outputPorts = new ArrayList<>();

        Tool.InputPort inputPort = new Tool.InputPort(1, "Input archive file with fasta", "REFERENCE_TAR", -37, 0);
        Tool.OutputPort outputPort = new Tool.OutputPort(1, "Unpacked fasta file", "${REFERENCE_TAR%.*}", 37, 0);

        inputPorts.add(inputPort);
        outputPorts.add(outputPort);

        Tool tool = new Tool();
        ReflectionTestUtils.setField(tool, "id", id);
        ReflectionTestUtils.setField(tool, "name", "Untar reference tar");
        ReflectionTestUtils.setField(tool, "command", "tar -xf ${REFERENCE_TAR}");
        ReflectionTestUtils.setField(tool, "image", "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/bwa:latest");
        ReflectionTestUtils.setField(tool, "inputPorts", inputPorts);
        ReflectionTestUtils.setField(tool, "outputPorts", outputPorts);

        return tool;
    }

//    public static Tool bwaIndex() {
//        List<Tool.Input> toolInputs = new ArrayList<>();
//        List<Tool.Output> toolOutputs = new ArrayList<>();
//
//        Tool.Input toolInput = new Tool.Input("REFERENCE_FASTA", false);
//        Tool.Output toolOutput = new Tool.Output("REFERENCE_FASTA", "${REFERENCE_FASTA}", false);
//
//        toolInputs.add(toolInput);
//        toolOutputs.add(toolOutput);
//
//        return new Tool(
//                "bwa-index",
//                toolInputs,
//                toolOutputs,
//                "bwa index ${REFERENCE_FASTA}",
//                "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/bwa:latest");
//    }



    public static Tool bwaMem(long id) {
        List<Tool.InputPort> inputPorts = new ArrayList<>();
        List<Tool.OutputPort> outputPorts = new ArrayList<>();

        Tool.InputPort toolInput1 = new Tool.InputPort(1, "Reference fasta", "REFERENCE_FASTA", -37, -20);
        Tool.InputPort toolInput2 = new Tool.InputPort(2, "Input_read_1", "INPUT_READ_1", -37, 0);
        Tool.InputPort toolInput3 = new Tool.InputPort(3, "Input_read_2", "INPUT_READ_2", -37, 20);
        Tool.OutputPort toolOutput = new Tool.OutputPort(1, "Aligned SAM", "${SAMPLE_ID}.sam", 37, 0);

        inputPorts.add(toolInput1);
        inputPorts.add(toolInput2);
        inputPorts.add(toolInput3);
        outputPorts.add(toolOutput);

        Tool tool = new Tool();
        ReflectionTestUtils.setField(tool, "id", id);
        ReflectionTestUtils.setField(tool, "name", "BWA MEM bundle");
        ReflectionTestUtils.setField(tool, "command", "bwa mem -M -R \"@RG\\tID:HWI\\tSM:${SAMPLE_ID}\\tPL:ILLUMINA\\tLB:MYSEQ\" -t 2 ${REFERENCE_FASTA} ${INPUT_READ_1} ${INPUT_READ_2} > ${SAMPLE_ID}.sam");
        ReflectionTestUtils.setField(tool, "image", "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/bwa:latest");
        ReflectionTestUtils.setField(tool, "inputPorts", inputPorts);
        ReflectionTestUtils.setField(tool, "outputPorts", outputPorts);

        return tool;
    }

    public static Tool samToolsView(long id) {
        List<Tool.InputPort> toolInputs = new ArrayList<>();
        List<Tool.OutputPort> toolOutputs = new ArrayList<>();

        Tool.InputPort toolInput = new Tool.InputPort(1, "Input SAM file", "SAM_FILE", -37, 0);
        Tool.OutputPort toolOutput = new Tool.OutputPort(1, "Output SAM file", "${SAMPLE_ID}.bam", 37, 0);

        toolInputs.add(toolInput);
        toolOutputs.add(toolOutput);

        Tool tool = new Tool();
        ReflectionTestUtils.setField(tool, "id", id);
        ReflectionTestUtils.setField(tool, "name", "SAMtools view");
        ReflectionTestUtils.setField(tool, "command", "samtools view -S -b ${SAM_FILE} > ${SAMPLE_ID}.bam");
        ReflectionTestUtils.setField(tool, "image", "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/samtools:1.16.1");
        ReflectionTestUtils.setField(tool, "inputPorts", toolInputs);
        ReflectionTestUtils.setField(tool, "outputPorts", toolOutputs);

        return tool;
    }

//    public static Tool samToolsSort() {
//        List<Tool.Input> toolInputs = new ArrayList<>();
//        List<Tool.Output> toolOutputs = new ArrayList<>();
//
//        Tool.Input toolInput = new Tool.Input("SAMPLE_BAM", true);
//        Tool.Output toolOutput = new Tool.Output("SORTED_BAM", "${SAMPLE_ID}.sorted.bam", true);
//
//        toolInputs.add(toolInput);
//        toolOutputs.add(toolOutput);
//
//        return new Tool(
//                "samtools-sort",
//                toolInputs,
//                toolOutputs,
//                "samtools sort ${SAMPLE_BAM} -o ${SAMPLE_ID}.sorted.bam",
//                "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/samtools:1.16.1");
//    }
//
//    public static Tool samToolsIndex() {
//        List<Tool.Input> toolInputs = new ArrayList<>();
//        List<Tool.Output> toolOutputs = new ArrayList<>();
//
//        Tool.Input toolInput = new Tool.Input("SORTED_BAM", true);
//        Tool.Output toolOutput = new Tool.Output("BAI_FILE", "${SORTED_BAM}.bai", true);
//
//        toolInputs.add(toolInput);
//        toolOutputs.add(toolOutput);
//
//        return new Tool(
//                "samtools-view",
//                toolInputs,
//                toolOutputs,
//                "samtools index ${SORTED_BAM}",
//                "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/samtools:1.16.1");
//    }
}
