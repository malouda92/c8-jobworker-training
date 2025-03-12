package com.camunda.academy.handler;

import com.camunda.academy.services.CustomerService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreditVerifyHandler {

    private final static Logger LOG = LogManager.getLogger(CreditVerifyHandler.class);
    private final CustomerService customerService;

    @Autowired
    public CreditVerifyHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    @JobWorker(type = "credit-verify", timeout = 200000, maxJobsActive = 1, requestTimeout = 5000, autoComplete = false)
    public void handle(JobClient client, ActivatedJob job, @Variable String customerId, @Variable Double orderTotal) {

        Double openAmount = customerService.deductCredit(customerId, orderTotal);

        client.newCompleteCommand(job.getKey()).variables(Map.of("openAmount", openAmount)).send();
    }
}
