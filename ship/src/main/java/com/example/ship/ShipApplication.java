package com.example.payment;

import io.camunda.zeebe.client.api.response.ActivatedJob;

import java.time.Instant;

import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 出库
 */

@SpringBootApplication
@EnableZeebeClient
@Slf4j
public class ShipApplication {
    public static void main(final String... args) {
        SpringApplication.run(ShipApplication.class, args);
    }

    //出库
    @ZeebeWorker(type = "shipment-service")
    public void handleShipJob(final JobClient client, final ActivatedJob job) {
        printfLog(job);
        client.newCompleteCommand(job.getKey()).variables("{\"shipment_count\":2}").send().join();
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