package com.example.demo.controller;

import com.example.demo.controller.dto.KubeJobStatusUpdateRequest;
import com.example.demo.service.PipelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KubeCallbackController {
    private final PipelineService pipelineService;

    @PostMapping("/result")
    public ResponseEntity<Object> ok(@RequestBody KubeJobStatusUpdateRequest request) {
        System.out.println("====callback=====");
        System.out.println(request);
        pipelineService.handleKubeJobResult(
                request.getPipelineJobId(),
                request.getPipelineTaskJobId(),
                request.getKubeJobId(),
                request.getJobStatus()
        );
        return ResponseEntity.ok().build();
    }
}
/*
0. 클린업 컨테이너 넣어야함. 라벨 지우고 rm -rf
1. 다음시퀀스 넘버를 실행시킨다
2. 다음 시퀀스 넘버가 없으면 다음 테스크를 실행시켜야하는데...
3. 태스크를 루프돌면서 taskId 로 검색해서 있으면 넘어가.. 없으면 인풋을 루프돌면서 sourceTaskId 가 모두끝났으면 실행하면됌 근데 이건 범요 알고리즘으로 사용하면될듯?
 */

/*
다음 잡을 찾는법..
finishedPipelineJobId, finishedTaskJobId, finishedKubeJobId

1. kubeJobId 를 통해 큐브잡을 가져오고 다음 sequenceId 를 찾아
2. 만약에 있으면 그거 돌리면되고
3. 없으면 tasks 를 루프돌면서 찾아야함
4. task 의 inputs 에서 sourceTaskId 가 모두 끝났는지 체크하고 해당 task 를 실행

 */