package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private Long id;
    private String name;
    private Double amount;
    private Integer payments;

    private int paymentsMade;

    private double interest;

    public ClientLoanDTO(ClientLoan clientLoan){
        id = clientLoan.getId();
        name = clientLoan.getLoan().getName();
        amount = clientLoan.getAmount();
        interest = clientLoan.getLoan().getInterest();
        payments = clientLoan.getPayments();
        paymentsMade = clientLoan.getPaymentsMade();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public int getPaymentsMade() {
        return paymentsMade;
    }

    public double getInterest() {
        return interest;
    }
}
