//package com.example.demo.service;
//
//import com.example.demo.entity.*;
//import com.example.demo.enums.*;
//import com.example.demo.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityNotFoundException;
//import java.io.IOException;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class PipelineService {
//    private final PipelineRepository pipelineRepository;
//    private final ToolRepository toolRepository;
//    private final UserFileRepository userFileRepository;
//    private final PipelineJobRepository pipelineJobRepository;
//    private final PipelineJobEnvRepository pipelineJobEnvRepository;
//    private final PipelineJobUserFileRepository pipelineJobUserFileRepository;
//    private final PipelineTaskJobRepository pipelineTaskJobRepository;
//    private final KubeJobRepository kubeJobRepository;
//
//    private final BashExecutor bashExecutor;
//    private final KubeClient kubeClient;
//
//    public void runPipeline(long pipelineId, List<PipelineJobInputUserFile> pipelineJobInputUserFiles) throws IOException, InterruptedException {
//        // Todo: pipeline 시작시에 해당 노드에 데이터 삭제. 남아있는 라벨... 이거는 잡 끝날때 라벨 삭제해줘야함 덮어쓰기되면 하고
//        Pipeline pipeline = pipelineRepository.findById(pipelineId)
//                .orElseThrow(() -> new EntityNotFoundException(String.format("Pipeline. id: %d", pipelineId)));
//        // pipelineJob 생성
//        PipelineJob pipelineJob = pipelineJobRepository.save(new PipelineJob(pipeline));
//
//        List<PipelineJobUserFile> pipelineJobUserFiles = new ArrayList<>();
//
//        for (PipelineJobInputUserFile pipelineJobInputUserFile: pipelineJobInputUserFiles) {
//            UserFile userFile = userFileRepository.findById(pipelineJobInputUserFile.getFileId())
//                    .orElseThrow(() -> new EntityNotFoundException(String.format("UserFile. Id: %d", pipelineJobInputUserFile.getFileId())));
//
//            Pipeline.Task task = pipeline.getTasks().stream()
//                    .filter(o -> o.getId().equals(pipelineJobInputUserFile.getPipelineTaskId()))
//                    .findAny()
//                    .orElseThrow(() -> new RuntimeException("input file task not found"));
//
//            Pipeline.Task.Input input = task.getInputs().stream()
//                    .filter(o -> o.getType().equals(PipelineTaskInputType.PIPELINE_INPUT))
//                    .filter(o -> o.getToolLabel().equals(pipelineJobInputUserFile.getPipelineTaskLabel()))
//                    .findAny()
//                    .orElseThrow(() -> new RuntimeException("input not found"));
//
//            pipelineJobUserFiles.add(new PipelineJobUserFile(pipelineJob, userFile, pipelineJobInputUserFile.getPipelineTaskLabel(), PipelineJobUserFileRelationType.INPUT, task.getId()));
//        }
//        pipelineJobUserFileRepository.saveAll(pipelineJobUserFiles);
//        List<PipelineTaskJob> executedPipelineTaskJob = pipelineTaskJobRepository.findByPipelineJobId(pipelineId);
//
//        runNewTask(pipelineJob.getId(), findNewTask(pipelineJob.getId(), executedPipelineTaskJob));
//    }
//
//    public void runNewTask(long pipelineJobId, Pipeline.Task pipelineTask) throws IOException, InterruptedException {
//        PipelineJob pipelineJob = pipelineJobRepository.getReferenceById(pipelineJobId);
//        PipelineTaskJob pipelineTaskJob = pipelineTaskJobRepository.save(new PipelineTaskJob(pipelineJob, pipelineTask.getId()));
//        System.out.println("new TaskId: " + pipelineTaskJob.getPipelineTaskId());
//
//        List<PipelineJobEnv> pipelineJobEnvs = pipelineJobEnvRepository.findPipelineJobEnvs(pipelineJobId);
//
//        List<PipelineJobUserFile> relatedInputFiles = pipelineJobUserFileRepository
//                .findInputs(pipelineJobId, PipelineJobUserFileRelationType.INPUT);
//
//        List<KubeJob> kubeJobs = new ArrayList<>();
//        int sequenceId = 1;
//
//        // init
//        kubeJobs.add(createLabelingKubeJob(pipelineTaskJob, sequenceId++, String.valueOf(pipelineJobId)));
//
//        // input
//        String sampleId = null;
//        List<PipelineJobEnv> newPipelineJobEnvs = new ArrayList<>();
//        for (Pipeline.Task.Input pipelineTaskInput: pipelineTask.getInputs()) {
//
//            // 파이프라인의 입력 파일이면
//            if (pipelineTaskInput.getType().equals(PipelineTaskInputType.PIPELINE_INPUT)) {
//                PipelineJobUserFile pipelineJobUserFile = relatedInputFiles.stream()
//                        .filter(o -> o.getLabel().equals(pipelineTaskInput.getToolLabel()))
//                        .findAny()
//                        .orElseThrow(() -> new EntityNotFoundException(String.format("input file not found. label: %s", pipelineTaskInput.getSourcePipelineTaskLabel())));
//                UserFile userFile = pipelineJobUserFile.getUserFile();
//                if (!userFile.getSampleId().equals("")) {
//                    if (sampleId == null) {
//                        sampleId = userFile.getSampleId();
//                    } else {
//                        if (!sampleId.equals(userFile.getSampleId())) {
//                            throw new RuntimeException(String.format("sample id different : %s", userFile.getSampleId()));
//                        }
//                    }
//                }
//
//                String fileName = pipelineJobUserFile.getUserFile().getS3Name();
//                PipelineJobEnv pipelineJobEnv = pipelineJobEnvRepository.save(new PipelineJobEnv(pipelineJob, pipelineTask.getId(), pipelineTaskInput.getToolLabel(), fileName));
//                newPipelineJobEnvs.add(pipelineJobEnv);
//                kubeJobs.add(createCopyInputJob(pipelineTaskJob, sequenceId++, pipelineJobUserFile.getUserFile()));
//            }
//
//            // 파이프라인 내 툴의 아웃풋일경우
//            if (pipelineTaskInput.getType().equals(PipelineTaskInputType.TOOL_OUTPUT)) {
//                List<PipelineJobEnv> sourceTaskEnvs = pipelineJobEnvs.stream()
//                        .filter(o -> o.getPipelineTaskId().equals(pipelineTaskInput.getSourcePipelineTaskId()))
//                        .collect(Collectors.toList());
//
//                PipelineJobEnv pipelineJobEnv1 = sourceTaskEnvs.stream()
//                        .filter(o -> o.getKeyName().equals(pipelineTaskInput.getSourcePipelineTaskLabel()))
//                        .findAny()
//                        .orElseThrow(() -> new EntityNotFoundException("output not found"));
//
//                PipelineJobEnv save = pipelineJobEnvRepository.save(new PipelineJobEnv(pipelineJob, pipelineTask.getId(), pipelineTaskInput.getToolLabel(), pipelineJobEnv1.getValue()));
//                newPipelineJobEnvs.add(save);
//
//                PipelineJobEnv pipelineJobEnv = sourceTaskEnvs.stream()
//                        .filter(o -> o.getKeyName().equals("SAMPLE_ID"))
//                        .findAny()
//                        .orElse(null);
//
//                if (pipelineJobEnv != null) {
//                    if (sampleId == null) {
//                        sampleId = pipelineJobEnv.getValue();
//                    } else {
//                        if (!sampleId.equals(pipelineJobEnv.getValue())) {
//                            throw new RuntimeException(String.format("sample id diff. %s, %s", sampleId));
//                        }
//                    }
//                }
//            }
//        }
//
//        if (sampleId != null) {
//            pipelineJobEnvs.add(pipelineJobEnvRepository.save(new PipelineJobEnv(pipelineJob, pipelineTask.getId(), "SAMPLE_ID", sampleId)));
//        }
//
//        // command
//        Tool tool = toolRepository.findById(pipelineTask.getToolId())
//                .orElseThrow(() -> new EntityNotFoundException(String.format("Tool. id: %d", pipelineTask.getToolId())));
//
//        kubeJobs.add(createMainJob(pipelineTaskJob, sequenceId++, tool.getImage(), tool.getCommand()));
//
//        for (Tool.Output output: tool.getOutputs()) {
//            Pipeline.Output output1 = pipelineJob.getPipeline().getOutputs().stream()
//                    .filter(o -> o.getPipelineTaskId().equals(pipelineTask.getId()))
//                    .filter(o -> o.getPipelineTaskLabel().equals(output.getLabel()))
//                    .findAny()
//                    .orElse(null);
//
//            // pipeline output 이 있는 경우
//            if (output1 != null) {
//                String resultFileName = bashExecutor.echo(output.getOutputName(), pipelineJobEnvs);
//                kubeJobs.add(createCopyResultToS3Job(pipelineTaskJob, sequenceId++, resultFileName));
//            }
//        }
//
//        kubeJobs = kubeJobRepository.saveAll(kubeJobs);
//
//
//
//        System.out.println("==================RUN NEW TASK==================");
//        for (KubeJob kubeJob: kubeJobs) {
//            System.out.println(kubeJob.getId() + " " + kubeJob.getImage() + " " + kubeJob.getCommand());
//        }
//        System.out.println("=================================================");
//        KubeJob kubeJob = kubeJobs.get(0);
//        kubeJob.setKubeJobStatus(JobStatus.PENDING);
//        kubeClient.runKubeJob("kube-job-" + kubeJob.getId(), kubeJob.getImage(), kubeJob.getCommand(), pipelineJobEnvs, pipelineJobId, pipelineTaskJob.getPipelineTaskId(), kubeJob.getId());
//    }
//
//
//    public Pipeline.Task findNewTask(long pipelineJobId, List<PipelineTaskJob> executedPipelineTaskJobs) {
//        PipelineJob pipelineJob = pipelineJobRepository.findById(pipelineJobId)
//                .orElseThrow(() -> new EntityNotFoundException(String.format("PipelineJob. Id: %d", pipelineJobId)));
//
//        Pipeline pipeline = pipelineJob.getPipeline();
//
//        for (Pipeline.Task pipelineTask: pipeline.getTasks()) {
//            boolean canRun = true;
//
//            // 이미 완료한거 제거
//            PipelineTaskJob pipelineTaskJob1 = executedPipelineTaskJobs.stream()
//                    .filter(o -> o.getPipelineTaskId().equals(pipelineTask.getId()))
//                    .findAny()
//                    .orElse(null);
//
//            if (pipelineTaskJob1 != null) {
//                continue;
//            }
//
//            for (Pipeline.Task.Input pipelineTaskInput: pipelineTask.getInputs()) {
//                if (pipelineTaskInput.getType().equals(PipelineTaskInputType.TOOL_OUTPUT)) {
//                    PipelineTaskJob pipelineTaskJob = executedPipelineTaskJobs.stream()
//                            .filter(o -> o.getPipelineTaskId().equals(pipelineTaskInput.getSourcePipelineTaskId()))
//                            .findAny()
//                            .orElseThrow(() -> new RuntimeException(pipelineTaskInput.getSourcePipelineTaskId() + ""));
//
//                    if (!pipelineTaskJob.isSuccess()) {
//                        canRun = false;
//                        break;
//                    }
//                }
//            }
//            if (canRun) {
//                return pipelineTask;
//            }
//        }
//        throw new RuntimeException(String.format("Can not find new task. pipelineId: %d", pipelineJobId));
//    }
//
//    private KubeJob createCleanupJob(PipelineTaskJob pipelineTaskJob, int sequenceId, JobStatus jobStatus) {
//        return new KubeJob(pipelineTaskJob, sequenceId, "rm -rf /data/*", "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/base");
//    }
//
//    private KubeJob createMainJob(PipelineTaskJob pipelineTaskJob, int sequenceId, String image, String command) {
//        return new KubeJob(pipelineTaskJob, sequenceId, command, image);
//    }
//
//    private KubeJob createCopyInputJob(PipelineTaskJob pipelineTaskJob, int sequenceId, UserFile userFile) {
//        return new KubeJob(pipelineTaskJob, sequenceId, String.format("aws s3 cp s3://mjkambucket/%s .", userFile.getS3Name()), "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/base:latest");
//    }
//
//    private KubeJob createCopyResultToS3Job(PipelineTaskJob pipelineTaskJob, int sequenceId, String fileName) {
//        return new KubeJob(pipelineTaskJob, sequenceId, String.format("aws s3 cp %s s3://mjkambucket/", fileName), "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/base:latest");
//    }
//
//    private KubeJob createLabelingKubeJob(PipelineTaskJob pipelineTaskJob, int sequenceId, String nodeLabel) {
//        return new KubeJob(pipelineTaskJob, sequenceId, String.format("kubectl label nodes $(MY_NODE_NAME) %s=%s", "pipeline-job-id", nodeLabel), "338282184009.dkr.ecr.ap-northeast-2.amazonaws.com/base:latest");
//    }
//
//    public void handleKubeJobResult(long pipelineJobId, int pipelineTaskId, Long kubeJobId, JobStatus jobStatus) throws IOException, InterruptedException {
//        KubeJob kubeJob = kubeJobRepository.findById(kubeJobId)
//                .orElseThrow(() -> new EntityNotFoundException(String.format("KubeJob. id: %d", kubeJobId)));
//
//        if (kubeJob.getStatus().equals(jobStatus)) {
//            return;
//        }
//        kubeJob.setKubeJobStatus(jobStatus);
//
//        PipelineJob pipelineJob = pipelineJobRepository.findById(pipelineJobId)
//                .orElseThrow(() -> new EntityNotFoundException(String.format("PipelineJob. id: %d", pipelineJobId)));
//
//        PipelineTaskJob pipelineTaskJob = pipelineTaskJobRepository.findOne(pipelineJobId, pipelineTaskId)
//                .orElseThrow(() -> new RuntimeException("PipelineTaskJob Not Found"));
//
//        if (jobStatus.equals(JobStatus.FAILED)) {
//            pipelineJob.setJobStatus(JobStatus.FAILED);
//            pipelineTaskJob.setTaskJobStatus(JobStatus.FAILED);
//        } else if (jobStatus.equals(JobStatus.RUNNING)) {
//            pipelineJob.setJobStatus(JobStatus.RUNNING);
//            pipelineTaskJob.setTaskJobStatus(JobStatus.RUNNING);
//        } else if (jobStatus.equals(JobStatus.SUCCESS)) {
//            KubeJob nextKubeJob = kubeJobRepository.findNextJob(pipelineJobId, pipelineTaskId, kubeJob.getSequenceId() + 1)
//                    .orElse(null);
//
//            // 툴 이 모두 끝남
//            if (nextKubeJob == null) {
//                saveResult(pipelineJobId, pipelineTaskId);
//
//                List<PipelineTaskJob> allTaskJobs = pipelineTaskJobRepository.findByPipelineJobId(pipelineJobId);
//
//                // 상태 업데이트 하고 전체 다 끝났는지 확인하기
//                PipelineTaskJob targetPipelineTaskJob = allTaskJobs.stream()
//                        .filter(o -> o.getPipelineTaskId().equals(pipelineTaskId))
//                        .findAny()
//                        .orElseThrow(() -> new RuntimeException(String.format("PipelineTaskJob Not found. taskId: %d", pipelineTaskId)));
//                targetPipelineTaskJob.setTaskJobStatus(jobStatus);
//
//                if (pipelineJob.getPipeline().getTasks().size() == allTaskJobs.size()) {
//                    pipelineJob.setJobStatus(JobStatus.SUCCESS);
//                } else {
//                    Pipeline.Task newTask = findNewTask(pipelineJobId, allTaskJobs);
//                    System.out.println("found new task: " + newTask.getId());
//                    runNewTask(pipelineJobId, newTask);
//                }
//
//            /*
//            1. 잡이 끝났다...
//            2. 어? 다음 잡이 없네.. 그럼 해당 테스크는 완료..
//            3. 그러면 파이프라인 전체가 끝난건지 확인..
//            4. 전체가 끝났다면 파이프라인 잡 상태 업데이트하고 끝
//            5. 아니면 새로운테스크 찾아서 실행
//             */
//            } else {
//                List<PipelineJobEnv> pipelineJobEnvs = pipelineJobEnvRepository.findPipelineJobEnvs(pipelineJobId);
//                nextKubeJob.setKubeJobStatus(JobStatus.PENDING);
//
//                System.out.println(nextKubeJob.getId() + " " + nextKubeJob.getImage() + " " + nextKubeJob.getCommand());
//
//                kubeClient.runKubeJob("kube-job-" + nextKubeJob.getId(), nextKubeJob.getImage(), nextKubeJob.getCommand(), pipelineJobEnvs, pipelineJobId, pipelineTaskJob.getPipelineTaskId(), nextKubeJob.getId());
//            }
//        }
//    }
//
//    private void saveResult(long pipelineJobId, int pipelineTaskId) throws IOException, InterruptedException {
//        PipelineJob pipelineJob = pipelineJobRepository.findById(pipelineJobId)
//                .orElseThrow(() -> new RuntimeException("PipelineJob: " + pipelineJobId));
//        Pipeline pipeline = pipelineJob.getPipeline();
//        Pipeline.Task pipelineTask = pipeline.getTasks().stream()
//                .filter(o -> o.getId().equals(pipelineTaskId))
//                .findAny()
//                .orElseThrow(() -> new RuntimeException("PipelineTask Not found by pipelineTaskId"));
//        Tool tool = toolRepository.findById(pipelineTask.getToolId())
//                .orElseThrow(() -> new EntityNotFoundException("tool. id: " + pipelineTask.getToolId()));
//
//        List<PipelineJobEnv> pipelineJobEnvs = pipelineJobEnvRepository.findPipelineJobEnvs(pipelineJobId);
//
//
//        for (Tool.Output output: tool.getOutputs()) {
//            String outputFileName = bashExecutor.echo(output.getOutputName(), pipelineJobEnvs);
//            pipelineJobEnvs.add(pipelineJobEnvRepository.save(new PipelineJobEnv(pipelineJob, pipelineTaskId, output.getLabel(), outputFileName)));
//
//            Pipeline.Output output1 = pipelineJob.getPipeline().getOutputs().stream()
//                    .filter(o -> o.getPipelineTaskId().equals(pipelineTask.getId()))
//                    .filter(o -> o.getPipelineTaskLabel().equals(output.getLabel()))
//                    .findAny()
//                    .orElse(null);
//
//            if (output1 != null) {
//                String resultFileName = bashExecutor.echo(output.getOutputName(), pipelineJobEnvs);
//                UserFile newUserFile;
//                if (output.isSample()) {
//                    PipelineJobEnv sampleIdEnv = pipelineJobEnvs.stream()
//                            .filter(o -> o.getKeyName().equals("SAMPLE_ID"))
//                            .findAny()
//                            .orElse(null);
//                    String sampleId = sampleIdEnv != null ? sampleIdEnv.getValue() : "";
//                    newUserFile = new UserFile(resultFileName, resultFileName, sampleId);
//                } else {
//                    newUserFile = new UserFile(resultFileName, resultFileName, "");
//                }
//                newUserFile = userFileRepository.save(newUserFile);
//
//                pipelineJobUserFileRepository.save(new PipelineJobUserFile(pipelineJob, newUserFile, output.getLabel(), PipelineJobUserFileRelationType.OUTPUT, pipelineTaskId));
//            }
//        }
//    }
//}
