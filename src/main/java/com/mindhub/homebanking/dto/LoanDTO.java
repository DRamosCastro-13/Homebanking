package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {

    private Long id;

    private String name;

    private List<Integer> payments;

    private Double maxAmount;

    public LoanDTO(Loan loan){
        id = loan.getId();
        name = loan.getName();
        payments = loan.getPayments();
        maxAmount = loan.getMaxAmount();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }
}
