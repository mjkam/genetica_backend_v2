package com.example.demo.service;

import com.example.demo.entity.PipelineJobEnv;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class KubeClient {
    private static final String API_VERSION = "batch/v1";
    private static final String KIND = "Job";
    private static final String NAMESPACE = "dev";
    private static final String VOLUME_NAME = "host-data-volume";
    private static final String SERVICE_ACCOUNT_NAME = "k8s-api-account";
    private static final String POD_RESTART_POLICY = "Never";
    private static final String IMAGE_PULL_POLICY = "Always";
    private static final Integer BACK_OFF_LIMIT = 0;
    private static final String NODE_NAME_ENV_KEY = "MY_NODE_NAME";

    private static final String SERVER_URL = "https://fa3e-175-195-237-20.ngrok.io/result";

    private final BatchV1Api apiInstance;

    public KubeClient() throws IOException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        this.apiInstance = new BatchV1Api(client);
    }

    public void runKubeJob(String jobName, String imageName, String command, List<PipelineJobEnv> pipelineJobEnvs, long pipelineJobId, long pipelineTaskId, long kubeJobId) {
        Map<String, String> totalEnvs = new HashMap<>();

        for (PipelineJobEnv pipelineJobEnv: pipelineJobEnvs) {
            totalEnvs.put(pipelineJobEnv.getKeyName(), pipelineJobEnv.getValue());
        }
        totalEnvs.put("SERVER_URL", SERVER_URL);
        totalEnvs.put("pipelineJobId", String.valueOf(pipelineJobId));
        totalEnvs.put("pipelineTaskId", String.valueOf(pipelineTaskId));
        totalEnvs.put("kubeJobId", String.valueOf(kubeJobId));

        command = "python3 /run.py RUNNING && " + command;
        command += " && " + "python3 /run.py SUCCESS";


        V1Job v1Job = v1Job(jobName, imageName, command, totalEnvs);

        try {
            apiInstance.createNamespacedJob("dev", v1Job, null, null, null, null);
        } catch (ApiException e) {
            System.err.println("Exception when calling BatchV1Api#createNamespacedJob");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }

    private V1Job v1Job(String jobName, String image, String args, Map<String, String> envs) {
        V1Job v1Job = new V1Job();
        v1Job.setApiVersion(API_VERSION);
        v1Job.setKind(KIND);
        v1Job.setMetadata(v1ObjectMeta(jobName));
        v1Job.setSpec(v1JobSpec(image, args, envs));

        return v1Job;
    }

    private V1ObjectMeta v1ObjectMeta(String jobName) {
        V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
        v1ObjectMeta.setName(jobName);
        v1ObjectMeta.setNamespace(NAMESPACE);

        return v1ObjectMeta;
    }

    private V1JobSpec v1JobSpec(String image, String args, Map<String, String> envs) {
        V1JobSpec v1JobSpec = new V1JobSpec();
        v1JobSpec.setTemplate(v1PodTemplateSpec(image, args, envs));
        v1JobSpec.setBackoffLimit(BACK_OFF_LIMIT);

        return v1JobSpec;
    }

    private V1PodTemplateSpec v1PodTemplateSpec(String image, String args, Map<String, String> envs) {
        V1PodTemplateSpec v1PodTemplateSpec = new V1PodTemplateSpec();
        v1PodTemplateSpec.setSpec(v1PodSpec(image, args, envs));

        return v1PodTemplateSpec;
    }

    private V1PodSpec v1PodSpec(String image, String args, Map<String, String> envs) {
        V1PodSpec v1PodSpec = new V1PodSpec();
        v1PodSpec.setServiceAccountName(SERVICE_ACCOUNT_NAME);
        v1PodSpec.setContainers(List.of(v1Container(image, args, envs)));
        v1PodSpec.setVolumes(hostVolumes());
        v1PodSpec.setRestartPolicy(POD_RESTART_POLICY);

        return v1PodSpec;
    }


    private V1Container v1Container(String image, String args, Map<String, String> envs) {
        V1VolumeMount v1VolumeMount = new V1VolumeMount();
        v1VolumeMount.setMountPath("/data");
        v1VolumeMount.setName(VOLUME_NAME);



        V1Container v1Container = new V1Container();
        v1Container.setName("worker");
        v1Container.setImage(image);
        v1Container.setImagePullPolicy(IMAGE_PULL_POLICY);
        v1Container.setCommand(Arrays.asList("/bin/sh", "-c"));
        v1Container.setArgs(List.of(args));
        v1Container.setEnv(containerEnvs(envs));
        v1Container.volumeMounts(List.of(v1VolumeMount));
        v1Container.setLifecycle(containerPostStart());
        v1Container.setLifecycle(containerPreStop());

        return v1Container;
    }

    private V1Lifecycle containerPostStart() {
        V1ExecAction v1ExecAction = new V1ExecAction();
        v1ExecAction.setCommand(Arrays.asList(
                "sh",
                "-c",
                "curl -d  '{\"pipelineJobId\":\"'\"$PIPELINE_JOB_ID\"'\", \"pipelineTaskJobId\":\"'\"$PIPELINE_TASK_JOB_ID\"'\", \"kubeJobId\":\"'\"$KUBE_JOB_ID\"'\", \"resultStatus\":\"RUNNING\"}' -H \"Content-Type: application/json\" -X POST $SERVER_URL"
        ));

        V1LifecycleHandler v1LifecycleHandler = new V1LifecycleHandler();
        v1LifecycleHandler.setExec(v1ExecAction);

        V1Lifecycle v1Lifecycle = new V1Lifecycle();
        v1Lifecycle.setPostStart(v1LifecycleHandler);

        return v1Lifecycle;
    }

    private V1Lifecycle containerPreStop() {
        V1ExecAction v1ExecAction = new V1ExecAction();
        v1ExecAction.setCommand(Arrays.asList(
                "sh",
                "-c",
                "curl -d  '{\"pipelineJobId\":\"'\"$PIPELINE_JOB_ID\"'\", \"pipelineTaskJobId\":\"'\"$PIPELINE_TASK_JOB_ID\"'\", \"kubeJobId\":\"'\"$KUBE_JOB_ID\"'\", \"resultStatus\":\"FAILED\"}' -H \"Content-Type: application/json\" -X POST $SERVER_URL"
        ));

        V1LifecycleHandler v1LifecycleHandler = new V1LifecycleHandler();
        v1LifecycleHandler.setExec(v1ExecAction);

        V1Lifecycle v1Lifecycle = new V1Lifecycle();
        v1Lifecycle.setPreStop(v1LifecycleHandler);

        return v1Lifecycle;
    }

    private List<V1EnvVar> containerEnvs(Map<String, String> envs) {
        List<V1EnvVar> result = new ArrayList<>();

        V1ObjectFieldSelector v1ObjectFieldSelector = new V1ObjectFieldSelector();
        v1ObjectFieldSelector.setFieldPath("spec.nodeName");

        V1EnvVarSource v1EnvVarSource = new V1EnvVarSource();
        v1EnvVarSource.setFieldRef(v1ObjectFieldSelector);

        V1EnvVar nodeNameEnv = new V1EnvVar();
        nodeNameEnv.setName(NODE_NAME_ENV_KEY);
        nodeNameEnv.setValueFrom(v1EnvVarSource);

        result.add(nodeNameEnv);

        for (String key: envs.keySet()) {
            V1EnvVar v1EnvVar = new V1EnvVar();
            v1EnvVar.setName(key);
            v1EnvVar.setValue(envs.get(key));
            result.add(v1EnvVar);
        }
        return result;
    }

    private List<V1Volume> hostVolumes() {
        List<V1Volume> result = new ArrayList<>();

        V1HostPathVolumeSource v1HostPathVolumeSource = new V1HostPathVolumeSource();
        v1HostPathVolumeSource.setPath("/data");
        v1HostPathVolumeSource.setType("DirectoryOrCreate");

        V1Volume hostDataVolume = new V1Volume();
        hostDataVolume.name(VOLUME_NAME);
        hostDataVolume.setHostPath(v1HostPathVolumeSource);

        result.add(hostDataVolume);
        return result;
    }
}
