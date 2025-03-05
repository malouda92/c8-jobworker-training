package com.camunda.academy.handler;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreditVerifyHandler {

    private final static Logger LOG = LogManager.getLogger(CreditVerifyHandler.class);

    @JobWorker(type = "credit-verify", timeout = 200000, maxJobsActive = 1, requestTimeout = 5000)
    public void handle(JobClient client, ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();

        Double orderTotal = (Double) variables.get("orderTotal");
        LOG.info("variable orderTotal= {}", orderTotal);

        Double customerCredit = (Double) variables.get("customerCredit");
        LOG.info("variable customerCredit= {}", customerCredit);

        client.newCompleteCommand(job.getKey()).send();
    }
}
