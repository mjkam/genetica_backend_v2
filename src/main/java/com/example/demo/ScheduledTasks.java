//package com.example.demo;
//
//import com.google.gson.reflect.TypeToken;
//import io.kubernetes.client.openapi.ApiClient;
//import io.kubernetes.client.openapi.ApiException;
//import io.kubernetes.client.openapi.Configuration;
//import io.kubernetes.client.openapi.apis.CoreV1Api;
//import io.kubernetes.client.openapi.models.*;
//import io.kubernetes.client.proto.V1;
//import io.kubernetes.client.proto.V1Batch;
//import io.kubernetes.client.util.Config;
//import io.kubernetes.client.util.Watch;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.concurrent.TimeUnit;
//import java.util.logging.Logger;
//
//@Component
//public class ScheduledTasks {
//    private final ApiClient client;
//
//    public ScheduledTasks() throws IOException {
//        this.client = Config.defaultClient();
//        Configuration.setDefaultApiClient(client);
//    }
//
//
//    @Scheduled(fixedRate = 1000)
//    public void reportCurrentTime() throws IOException, ApiException {
//        CoreV1Api api = new CoreV1Api();
//
//        Watch<V1Pod> watch = Watch.createWatch(
//                client,
//                api.listPodForAllNamespacesCall(null, null, null, null, null, null, null, null, 10000,Boolean.TRUE,  null),
//                new TypeToken<Watch.Response<V1Pod>>(){}.getType());
//
//        for (Watch.Response<V1Pod> item : watch) {
//            System.out.println(item.type + " " + item.status);
//            V1Pod v1Pod = item.object;
//            System.out.println(v1Pod.getSpec().getNodeName());
////            V1Pod v1Pod = item.object;
////            System.out.println(v1Pod.getSpec().getNodeName());
////            System.out.printf("%s : %s%n", item.type, item.object.getMetadata().getName());
//        }
//    }
//
////    public void test() {
////        V1Job v1Job = new V1Job();
////        v1Job.
////    }
//}
