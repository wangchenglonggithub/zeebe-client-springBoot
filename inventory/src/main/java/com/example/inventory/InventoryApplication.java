package com.example.inventory;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Instant;

@SpringBootApplication
@EnableZeebeClient
@Slf4j
public class InventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }
    //支付服务
    @ZeebeWorker(type = "inventory-service")
    public void handlePaymentJob(final JobClient client, final ActivatedJob job) {
        //todo 业务逻辑
        printfLog(job);
        client.newCompleteCommand(job.getKey()).variables("{\"inventory_count\": 11111}").send().join();
    }


    /**
     * 打印日志
     */
    private static void printfLog(final ActivatedJob job) {
        log.info(
                "complete job(完成作业参数)\n>>> [type: {}, key: {}, element: {}, workflow instance: {}]\n{deadline; {}]\n[headers: {}]\n[variables: {}]",
                job.getType(),
                job.getKey(),
                job.getElementId(),
                job.getProcessInstanceKey(),
                Instant.ofEpochMilli(job.getDeadline()),
                job.getCustomHeaders(),
                job.getVariables());
    }
}
