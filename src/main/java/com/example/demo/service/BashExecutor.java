package com.example.demo.service;

import com.example.demo.entity.PipelineTaskJobEnv;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class BashExecutor {
    public String echo(String expression, List<PipelineTaskJobEnv> pipelineTaskJobEnvs) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();
        ProcessBuilder processBuilder = builder.command("sh", "-c", String.format("echo %s", expression));
        pipelineTaskJobEnvs.forEach(o -> processBuilder.environment().put(o.getKeyName(), o.getValue()));
        Process process = processBuilder.start();

        BufferedReader stdOutBufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdErrBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        boolean finished = process.waitFor(5, TimeUnit.SECONDS);

        if (!finished) {
            String result = stdErrBufferedReader.lines().collect(Collectors.joining());
            throw new RuntimeException(result);
        } else {
            return stdOutBufferedReader.lines().collect(Collectors.joining());
        }
    }
}
