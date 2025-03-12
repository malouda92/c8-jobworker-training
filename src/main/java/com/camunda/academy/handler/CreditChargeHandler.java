package com.camunda.academy.handler;

import com.camunda.academy.services.CreditCardService;
import com.google.errorprone.annotations.Var;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreditChargeHandler {

    private final static Logger LOG = LogManager.getLogger(CreditChargeHandler.class);
    private final CreditCardService creditCardService;

    public CreditChargeHandler(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }


    @JobWorker(type = "credit-charge", timeout = 200000, maxJobsActive = 1, requestTimeout = 5000, fetchAllVariables = true)
    public void handle(JobClient client, ActivatedJob job, @Variable String cardNumber, @Variable String cvc, @Variable String expiryDate, @Variable Double openAmount) {

        try {
            creditCardService.chargeAmount(cardNumber,cvc,expiryDate,openAmount);
            client.newCompleteCommand(job.getKey()).send();
        } catch (IllegalArgumentException e) {
            client.newThrowErrorCommand(job.getKey())
                    .errorCode("DateExpiryError")
                    .send().join();
        }

    }
}
