package com.example.demo;

import com.example.demo.entity.Tool;

import java.util.ArrayList;
import java.util.List;

public class ToolFactory {
    public static Tool unTar() {
        List<Tool.Input> toolInputs = new ArrayList<>();
        List<Tool.Output> toolOutputs = new ArrayList<>();

        Tool.Input toolInput = new Tool.Input("REFERENCE_TAR", false);
        Tool.Output toolOutput = new Tool.Output("REFERENCE_FASTA", "${REFERENCE_TAR%.*}", false);

        toolInputs.add(toolInput);
        toolOutputs.add(toolOutput);

        return new Tool(
                "untar-reference",
                toolInputs,
                toolOutputs,
                "tar -xf ${REFERENCE_TAR}",
                "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/base:latest");
    }

    public static Tool bwaIndex() {
        List<Tool.Input> toolInputs = new ArrayList<>();
        List<Tool.Output> toolOutputs = new ArrayList<>();

        Tool.Input toolInput = new Tool.Input("REFERENCE_FASTA", false);
        Tool.Output toolOutput = new Tool.Output("REFERENCE_FASTA", "${REFERENCE_FASTA}", false);

        toolInputs.add(toolInput);
        toolOutputs.add(toolOutput);

        return new Tool(
                "bwa-index",
                toolInputs,
                toolOutputs,
                "bwa index ${REFERENCE_FASTA}",
                "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/bwa:latest");
    }



    public static Tool bwaMem() {
        List<Tool.Input> toolInputs = new ArrayList<>();
        List<Tool.Output> toolOutputs = new ArrayList<>();

        Tool.Input toolInput1 = new Tool.Input("REFERENCE_FASTA", false);
        Tool.Input toolInput2 = new Tool.Input("SAMPLE_READ_1", true);
        Tool.Input toolInput3 = new Tool.Input("SAMPLE_READ_2", true);
        Tool.Output toolOutput = new Tool.Output("SAMPLE_SAM", "${SAMPLE_ID}.sam", true);

        toolInputs.add(toolInput1);
        toolInputs.add(toolInput2);
        toolInputs.add(toolInput3);
        toolOutputs.add(toolOutput);

        return new Tool(
                "bwa-mem",
                toolInputs,
                toolOutputs,
                "bwa mem -M -R \"@RG\\tID:HWI\\tSM:${SAMPLE_ID}\\tPL:ILLUMINA\\tLB:MYSEQ\" -t 2 ${REFERENCE_FASTA} ${SAMPLE_READ_1} ${SAMPLE_READ_2} > ${SAMPLE_ID}.sam",
                "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/bwa:latest");
    }

    public static Tool samToolsView() {
        List<Tool.Input> toolInputs = new ArrayList<>();
        List<Tool.Output> toolOutputs = new ArrayList<>();

        Tool.Input toolInput = new Tool.Input("SAM_FILE", true);
        Tool.Output toolOutput = new Tool.Output("BAM_FILE", "${SAMPLE_ID}.bam", true);

        toolInputs.add(toolInput);
        toolOutputs.add(toolOutput);

        return new Tool(
                "samtools-view",
                toolInputs,
                toolOutputs,
                "samtools view -S -b ${SAM_FILE} > ${SAMPLE_ID}.bam",
                "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/samtools:1.16.1");
    }

    public static Tool samToolsSort() {
        List<Tool.Input> toolInputs = new ArrayList<>();
        List<Tool.Output> toolOutputs = new ArrayList<>();

        Tool.Input toolInput = new Tool.Input("SAMPLE_BAM", true);
        Tool.Output toolOutput = new Tool.Output("SORTED_BAM", "${SAMPLE_ID}.sorted.bam", true);

        toolInputs.add(toolInput);
        toolOutputs.add(toolOutput);

        return new Tool(
                "samtools-sort",
                toolInputs,
                toolOutputs,
                "samtools sort ${SAMPLE_BAM} -o ${SAMPLE_ID}.sorted.bam",
                "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/samtools:1.16.1");
    }

    public static Tool samToolsIndex() {
        List<Tool.Input> toolInputs = new ArrayList<>();
        List<Tool.Output> toolOutputs = new ArrayList<>();

        Tool.Input toolInput = new Tool.Input("SORTED_BAM", true);
        Tool.Output toolOutput = new Tool.Output("BAI_FILE", "${SORTED_BAM}.bai", true);

        toolInputs.add(toolInput);
        toolOutputs.add(toolOutput);

        return new Tool(
                "samtools-view",
                toolInputs,
                toolOutputs,
                "samtools index ${SORTED_BAM}",
                "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/samtools:1.16.1");
    }
//
//    public static Tool gatk() {
//
//    }
}
