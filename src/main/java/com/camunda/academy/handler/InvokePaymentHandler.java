package com.camunda.academy.handler;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvokePaymentHandler {

    private final ZeebeClient zeebeClient;

    @Autowired
    public InvokePaymentHandler(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @JobWorker(type = "lancer-paiement", autoComplete = false)
    public void handlePaymentInvocation(JobClient jobClient, ActivatedJob job) {

        // Fetch variables from order process & generate random order ID
        Map<String, Object> orderVariables = job.getVariablesAsMap();
        int orderId = new Random().nextInt();
        orderVariables.put("orderId", orderId);

        // Start payment process
        zeebeClient.newPublishMessageCommand()
                .messageName("invoke_paiement")
                .withoutCorrelationKey()
                .variables(orderVariables)
                .send().join();

        // Complete the job in the order process
        jobClient.newCompleteCommand(job.getKey()).variable("orderId", orderId).send();
    }
}