package com.camunda.academy.workers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.camunda.academy.services.CreditCardService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class CreditCardChargingWorker {

@Autowired CreditCardService creditCardService;

Logger LOGGER = LoggerFactory.getLogger(CreditCardChargingWorker.class);

@JobWorker(type = "credit-card-charging", autoComplete = false)
public void handleCreditCardCharging(final JobClient jobClient, final ActivatedJob job) {
LOGGER.info("Task definition type: " + job.getType());

    Map variables = job.getVariablesAsMap();
    String cardNumber = variables.get("cardNumber").toString();
    String cvc = variables.get("cvc").toString();
    String expiryDate = variables.get("expiryDate").toString();
    Double amount = Double.valueOf(variables.get("openAmount").toString());

    try {
      new CreditCardService().chargeAmount(cardNumber, cvc, expiryDate, amount);

      jobClient.newCompleteCommand(job).send().join();
    } catch (Exception e) {
      jobClient.newFailCommand(job).retries(3).errorMessage(e.getMessage()).send().join();
    }

}

}
