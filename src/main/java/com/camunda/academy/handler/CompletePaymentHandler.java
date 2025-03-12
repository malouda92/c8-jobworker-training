package com.camunda.academy.handler;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompletePaymentHandler {

    private final ZeebeClient zeebeClient;

    @Autowired
    public CompletePaymentHandler(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @JobWorker(type = "paiement_complete", autoComplete = false)
    public void handlePaymentCompletion(JobClient jobClient, ActivatedJob job) {
        String messageName = (String) job.getVariable("messageName");
        zeebeClient.newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(String.valueOf(job.getVariable("orderId")))
                .send().join();
        
        jobClient.newCompleteCommand(job.getKey()).send();
    }
}