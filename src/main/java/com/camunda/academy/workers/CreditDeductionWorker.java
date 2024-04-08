package com.camunda.academy.workers;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.camunda.academy.services.CustomerService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;

import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class CreditDeductionWorker {

  @Autowired CustomerService customerService;
  Logger LOGGER = LoggerFactory.getLogger(CreditDeductionWorker.class);

  @JobWorker(type = "credit-deduction", autoComplete = false)
  public void handleCreditDeduction(final JobClient jobClient, final ActivatedJob job) {
    Map<String, Object> variables = job.getVariablesAsMap();
    String customerId = variables.get("customerId").toString();
    Double orderTotal = Double.valueOf(variables.get("orderTotal").toString());

    double customerCredit = customerService.getCustomerCredit(customerId);
    double openAmount = customerService.deductCredit(customerCredit, orderTotal);

    
    Map newVariables = new HashMap();
    newVariables.put("customerCredit", customerCredit);
    newVariables.put("openAmount", openAmount);

    jobClient.newCompleteCommand(job).variables(newVariables).send().join();

  }
}