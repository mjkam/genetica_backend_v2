package com.example.demo.service;

import com.example.demo.entity.KubeJob;
import com.example.demo.entity.Tool;
import com.example.demo.entity.UserFile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class KubeJobFactory {
    private final static String BASE_IMAGE = "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/base:latest";
    private final static String LABELING_CMD = "rm -rf * && kubectl label nodes $(MY_NODE_NAME) pipeline-job-id=%d";
    private final static String COPY_TO_S3_CMD = "aws s3 cp %s s3://mjkambucket/";
    private final static String COPY_FROM_S3_CMD = "aws s3 cp s3://mjkambucket/%s .";


    public KubeJob mainKubeJob(long pipelineTaskJobId, int sequenceId, String image, String command) {
        return new KubeJob(pipelineTaskJobId, sequenceId, image, command);
    }

    public KubeJob copyInputFromS3KubeJob(long pipelineTaskJobId, int sequenceId, String fileName) {
        return new KubeJob(
                pipelineTaskJobId,
                sequenceId,
                BASE_IMAGE,
                String.format(COPY_FROM_S3_CMD, fileName));
    }

    public KubeJob copyResultToS3KubeJob(long pipelineTaskJobId, int sequenceId, String fileName) {
        return new KubeJob(
                pipelineTaskJobId,
                sequenceId,
                BASE_IMAGE,
                String.format(COPY_TO_S3_CMD, fileName));
    }

    public KubeJob labelingKubeJob(long pipelineJobId, long pipelineTaskJobId, int sequenceId) {
        return new KubeJob(
                pipelineTaskJobId,
                sequenceId,
                BASE_IMAGE,
                String.format(LABELING_CMD, pipelineJobId));
    }
}
