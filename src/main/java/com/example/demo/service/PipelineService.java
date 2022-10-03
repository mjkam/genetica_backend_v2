package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.enums.PipelineJobUserFileRelationType;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PipelineService {
    private final PipelineRepository pipelineRepository;
    private final ToolRepository toolRepository;
    private final PipelineJobRepository pipelineJobRepository;
    private final PipelineTaskJobRepository pipelineTaskJobRepository;
    private final PipelineJobUserFileRepository pipelineJobUserFileRepository;
    private final UserFileRepository userFileRepository;
    private final KubeJobRepository kubeJobRepository;
    private final PipelineTaskJobEnvRepository pipelineTaskJobEnvRepository;
    private final KubeJobFactory kubeJobFactory;
    private final BashExecutor bashExecutor;
    private final KubeClient kubeClient;

    public List<Pipeline> getPipeline() {
        return pipelineRepository.findAll();
    }

    public long executePipeline(long pipelineId, List<PipelineInputUserFile> pipelineInputUserFiles) throws IOException, InterruptedException {
        LocalDateTime currentDateTime = LocalDateTime.now();

        PipelineJob pipelineJob = pipelineJobRepository.save(new PipelineJob(pipelineId, currentDateTime));
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> new EntityNotFoundException("Pipeline. id: " + pipelineId));

        List<PipelineJobUserFile> pipelineJobUserFiles = new ArrayList<>();
        for (PipelineInputUserFile pipelineInputUserFile: pipelineInputUserFiles) {
            Pipeline.InputFile inputFile = pipeline.getInputFile(pipelineInputUserFile.getInputFileId());

            PipelineJobUserFile pipelineJobUserFile = new PipelineJobUserFile(
                    pipelineJob.getId(),
                    inputFile.getTaskId(),
                    inputFile.getPortId(),
                    pipelineInputUserFile.getUserFileId(),
                    PipelineJobUserFileRelationType.INPUT);
            pipelineJobUserFiles.add(pipelineJobUserFile);
        }
        pipelineJobUserFileRepository.saveAll(pipelineJobUserFiles);

        Pipeline.Task executableTask = pipeline.findExecutableTask(new ArrayList<>());

        run(pipeline, pipelineJobUserFiles, new ArrayList<>(), executableTask, pipelineJob.getId(), currentDateTime);

        return pipelineId;
    }

    public void run(
            Pipeline pipeline,
            List<PipelineJobUserFile> pipelineJobUserFiles,
            List<PipelineTaskJobEnv> pipelineTaskJobEnvs,
            Pipeline.Task executableTask,
            long pipelineJobId,
            LocalDateTime currentDateTime) throws IOException, InterruptedException {
        PipelineTaskJob pipelineTaskJob = pipelineTaskJobRepository.save(new PipelineTaskJob(pipelineJobId, executableTask.getId(), currentDateTime));

        Tool tool = toolRepository.findById(executableTask.getToolId())
                .orElseThrow(() -> new EntityNotFoundException("Tool. id: " + executableTask.getToolId()));

        int sequence = 1;
        List<KubeJob> kubeJobs = new ArrayList<>();
        if (pipelineTaskJobEnvs.size() == 0) {
            kubeJobs.add(kubeJobFactory.labelingKubeJob(pipelineJobId, pipelineTaskJob.getId(), sequence++));
        }


        List<PipelineTaskJobEnv> newPipelineTaskJobEnvs = new ArrayList<>();
        String sampleId = "";
        for (Tool.InputPort inputPort: tool.getInputPorts()) {
            PipelineJobUserFile pipelineJobUserFile = pipelineJobUserFiles.stream()
                    .filter(o -> o.getPipelineTaskId().equals(executableTask.getId()))
                    .filter(o -> o.getPipelineTaskPortId().equals(inputPort.getId()))
                    .filter(o -> o.getPipelineJobUserFileRelationType().equals(PipelineJobUserFileRelationType.INPUT))
                    .findAny()
                    .orElse(null);

            if (pipelineJobUserFile == null) {
                Pipeline.ConnectEdge connectEdge = pipeline.getTask(pipelineTaskJob.getPipelineTaskId(), inputPort.getId());
                long toolId = pipeline.getToolId(connectEdge.getSourceTaskId());

                Tool tool1 = toolRepository.findById(toolId)
                        .orElseThrow(() -> new EntityNotFoundException(toolId + ""));
                String outputName = tool1.getOutputName(connectEdge.getSourceOutputPortId());

                List<PipelineTaskJobEnv> filteredEnvs = pipelineTaskJobEnvs.stream()
                        .filter(o -> o.getPipelineTaskId().equals(connectEdge.getSourceTaskId()))
                        .collect(Collectors.toList());
                String echo = bashExecutor.echo(outputName, filteredEnvs);
//                kubeJobs.add(kubeJobFactory.copyInputFromS3KubeJob(pipelineTaskJob.getId(), sequence++, echo));
                newPipelineTaskJobEnvs.add(new PipelineTaskJobEnv(pipelineJobId, pipelineTaskJob.getPipelineTaskId(), inputPort.getVarName(), echo));

                PipelineTaskJobEnv sample_id = filteredEnvs.stream()
                        .filter(o -> o.getKeyName().equals("SAMPLE_ID"))
                        .findAny()
                        .orElse(null);
                if (sample_id != null) {
                    if (sampleId.equals("")) {
                        sampleId = sample_id.getValue();
                    } else {
                        if (!sampleId.equals(sample_id.getValue())) {
                            throw new RuntimeException("sampleId not match: " + sample_id.getValue());
                        }
                    }
                }
            } else {
                UserFile userFile = userFileRepository.findById(pipelineJobUserFile.getUserFileId())
                        .orElseThrow(() -> new EntityNotFoundException("UserFile: " + pipelineJobUserFile.getUserFileId()));
                kubeJobs.add(kubeJobFactory.copyInputFromS3KubeJob(pipelineTaskJob.getId(), sequence++, userFile.getS3Name()));
                newPipelineTaskJobEnvs.add(new PipelineTaskJobEnv(pipelineJobId, pipelineTaskJob.getPipelineTaskId(), inputPort.getVarName(), userFile.getName()));
                if (sampleId.equals("")) {
                    sampleId = userFile.getSampleId();
                } else {
                    if (!sampleId.equals(userFile.getSampleId())) {
                        throw new RuntimeException("sampleId not match: " + userFile.getSampleId());
                    }
                }
            }
        }
        newPipelineTaskJobEnvs.add(new PipelineTaskJobEnv(pipelineJobId, pipelineTaskJob.getPipelineTaskId(), "SAMPLE_ID", sampleId));

        kubeJobs.add(kubeJobFactory.mainKubeJob(pipelineTaskJob.getId(), sequence++, tool.getImage(), tool.getCommand()));



        for (Tool.OutputPort outputPort : tool.getOutputPorts()) {
            if (pipeline.isOutput(pipelineTaskJob.getPipelineTaskId(), outputPort.getId())) {
                String echo = bashExecutor.echo(outputPort.getOutputName(), newPipelineTaskJobEnvs);
                kubeJobs.add(kubeJobFactory.copyResultToS3KubeJob(pipelineTaskJob.getId(), sequence++, echo));
            }
        }

        pipelineTaskJobEnvRepository.saveAll(newPipelineTaskJobEnvs);
        kubeJobRepository.saveAll(kubeJobs);

        KubeJob targetJob = kubeJobs.get(0);
        System.out.println("=======New Task==========");
        System.out.println(targetJob);
        kubeClient.runKubeJob(
                "kubejob-" + targetJob.getId(),
                targetJob.getImage(),
                targetJob.getCommand(),
                newPipelineTaskJobEnvs,
                pipelineJobId,
                pipelineTaskJob.getId(),
                targetJob.getId());
    }
}
