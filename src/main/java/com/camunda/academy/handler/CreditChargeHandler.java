package com.camunda.academy.handler;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreditChargeHandler {

    private final static Logger LOG = LoggerFactory.getLogger(CreditChargeHandler.class);

    @JobWorker(type = "credit-charge", timeout = 200000, maxJobsActive = 1, requestTimeout = 5000)
    public void handle(JobClient client, ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        Double orderTotal = (Double) variables.get("orderTotal");
        LOG.info("variable orderTotal= {}", orderTotal);
        Double customerCredit = (Double) variables.get("customerCredit");
        LOG.info("variable customerCredit= {}", customerCredit);

        client.newCompleteCommand(job.getKey()).send();
    }
}
