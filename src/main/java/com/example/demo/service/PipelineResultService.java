package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.enums.JobStatus;
import com.example.demo.enums.PipelineJobUserFileRelationType;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PipelineResultService {
    private final KubeJobRepository kubeJobRepository;
    private final PipelineJobRepository pipelineJobRepository;
    private final PipelineTaskJobRepository pipelineTaskJobRepository;
    private final PipelineRepository pipelineRepository;
    private final PipelineTaskJobEnvRepository pipelineTaskJobEnvRepository;
    private final PipelineJobUserFileRepository pipelineJobUserFileRepository;
    private final ToolRepository toolRepository;
    private final BashExecutor bashExecutor;
    private final UserFileRepository userFileRepository;
    private final KubeClient kubeClient;

    private final PipelineService pipelineService;


    public void handleKubeJobResult(long pipelineJobId, int pipelineTaskId, long kubeJobId, JobStatus jobStatus) throws IOException, InterruptedException {
        KubeJob kubeJob = kubeJobRepository.findById(kubeJobId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("KubeJob. id: %d", kubeJobId)));

        if (kubeJob.getStatus().equals(jobStatus)) {
            return;
        }
        kubeJob.setKubeJobStatus(jobStatus);

        PipelineJob pipelineJob = pipelineJobRepository.findById(pipelineJobId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("PipelineJob. id: %d", pipelineJobId)));

        PipelineTaskJob pipelineTaskJob = pipelineTaskJobRepository.findOne(pipelineJobId, pipelineTaskId)
                .orElseThrow(() -> new RuntimeException("PipelineTaskJob Not Found"));

        Pipeline pipeline = pipelineRepository.findById(pipelineJob.getPipelineId())
                .orElseThrow(() -> new EntityNotFoundException("" + pipelineJob.getPipelineId()));

        List<PipelineTaskJobEnv> pipelineTaskJobEnvs = pipelineTaskJobEnvRepository.findByPipelineJobId(pipelineJobId);
        List<PipelineJobUserFile> pipelineJobUserFiles = pipelineJobUserFileRepository.findPipelineInputUserFiles(pipelineJobId, PipelineJobUserFileRelationType.INPUT);

        if (jobStatus.equals(JobStatus.FAILED)) {
            pipelineJob.setJobStatus(JobStatus.FAILED);
            pipelineTaskJob.setTaskJobStatus(JobStatus.FAILED);
        } else if (jobStatus.equals(JobStatus.RUNNING)) {
            pipelineJob.setJobStatus(JobStatus.RUNNING);
            pipelineTaskJob.setTaskJobStatus(JobStatus.RUNNING);
        } else if (jobStatus.equals(JobStatus.SUCCESS)) {
            KubeJob nextKubeJob = kubeJobRepository.findNextJob(pipelineTaskJob.getId(), kubeJob.getSequence() + 1)
                    .orElse(null);

            // ??? ??? ?????? ??????
            if (nextKubeJob == null) {
                saveResult(pipelineJob, pipelineTaskJob, pipeline, pipelineTaskJobEnvs, pipelineJobUserFiles);

                List<PipelineTaskJob> allTaskJobs = pipelineTaskJobRepository.findByPipelineJobId(pipelineJobId);

                // ?????? ???????????? ?????? ?????? ??? ???????????? ????????????
                PipelineTaskJob targetPipelineTaskJob = allTaskJobs.stream()
                        .filter(o -> o.getPipelineTaskId().equals(pipelineTaskId))
                        .findAny()
                        .orElseThrow(() -> new RuntimeException(String.format("PipelineTaskJob Not found. taskId: %d", pipelineTaskId)));
                targetPipelineTaskJob.setTaskJobStatus(jobStatus);

                if (pipeline.getTasks().size() == allTaskJobs.size()) {
                    pipelineJob.setJobStatus(JobStatus.SUCCESS);
                } else {
                    Pipeline.Task executableTask = pipeline.findExecutableTask(
                            allTaskJobs.stream()
                                    .filter(o -> o.getStatus().equals(JobStatus.SUCCESS))
                                    .map(PipelineTaskJob::getPipelineTaskId).collect(Collectors.toList())
                    );

                    pipelineService.run(
                            pipeline, pipelineJobUserFiles, pipelineTaskJobEnvs, executableTask, pipelineJobId, LocalDateTime.now()
                    );
                }

            /*
            1. ?????? ?????????...
            2. ???? ?????? ?????? ??????.. ?????? ?????? ???????????? ??????..
            3. ????????? ??????????????? ????????? ???????????? ??????..
            4. ????????? ???????????? ??????????????? ??? ?????? ?????????????????? ???
            5. ????????? ?????????????????? ????????? ??????
             */
            } else {
                List<PipelineTaskJobEnv> collect = pipelineTaskJobEnvs.stream()
                        .filter(o -> o.getPipelineTaskId().equals(pipelineTaskId))
                        .collect(Collectors.toList());

                nextKubeJob.setKubeJobStatus(JobStatus.PENDING);

                System.out.println(nextKubeJob.getId() + " " + nextKubeJob.getImage() + " " + nextKubeJob.getCommand());

                kubeClient.runKubeJob("kube-job-" + nextKubeJob.getId(), nextKubeJob.getImage(), nextKubeJob.getCommand(), collect, pipelineJobId, pipelineTaskJob.getPipelineTaskId(), nextKubeJob.getId());
            }
        }
    }

    private void saveResult(PipelineJob pipelineJob, PipelineTaskJob pipelineTaskJob, Pipeline pipeline, List<PipelineTaskJobEnv> pipelineTaskJobEnvs, List<PipelineJobUserFile> pipelineJobUserFiles) throws IOException, InterruptedException {
        long toolId = pipeline.getToolId(pipelineTaskJob.getPipelineTaskId());
        Tool tool = toolRepository.findById(toolId)
                .orElseThrow(() -> new EntityNotFoundException("" + toolId));

        List<PipelineTaskJobEnv> taskJobEnvs = pipelineTaskJobEnvs.stream()
                .filter(o -> o.getPipelineTaskId().equals(pipelineTaskJob.getPipelineTaskId()))
                .collect(Collectors.toList());

        for (Tool.OutputPort outputPort : tool.getOutputPorts()) {
            String echo = bashExecutor.echo(outputPort.getOutputName(), taskJobEnvs);

            PipelineTaskJobEnv sample_id = taskJobEnvs.stream()
                    .filter(o -> o.getKeyName().equals("SAMPLE_ID"))
                    .findAny()
                    .orElse(null);
            if (pipeline.isOutput(pipelineTaskJob.getPipelineTaskId(), outputPort.getId())) {
                UserFile userFile;
                if (sample_id == null) {
                    userFile = new UserFile(echo, echo, 132940, "/", "", LocalDateTime.now());
                } else {
                    userFile = new UserFile(echo, echo, 54132, "/", sample_id.getValue(), LocalDateTime.now());
                }
                userFile = userFileRepository.save(userFile);
                pipelineJobUserFiles.add(pipelineJobUserFileRepository.save(new PipelineJobUserFile(pipelineJob.getId(), pipelineTaskJob.getPipelineTaskId(), outputPort.getId(), userFile.getId(), PipelineJobUserFileRelationType.OUTPUT)));
            }
        }
    }
}
