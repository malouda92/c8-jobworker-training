package com.camunda.academy.services;

import org.springframework.stereotype.Component;

@Component
public class CreditCardService {

	public void chargeAmount(String cardNumber, String cvc, String expiryDate, Double amount) {
		if (!isExpiryDateValid(expiryDate)) throw new IllegalArgumentException("Date invalide");
		System.out.printf("charging card %s that expires on %s and has cvc %s with amount of %f %s", cardNumber, expiryDate, cvc, amount, System.lineSeparator());
		System.out.println("payment completed");

	}

	public boolean isExpiryDateValid(String expiryDate) {
		return expiryDate.length() == 5;
	}
}
