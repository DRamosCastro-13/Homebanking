package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDate;

public class TransactionDTO {
    private Long id;
    private TransactionType type;
    private String description;
    private Double amount;
    private LocalDate date;

    public TransactionDTO(Transaction transaction){
        id = transaction.getId();
        type = transaction.getType();
        description = transaction.getDescription();
        amount = transaction.getAmount();
        date = transaction.getDate();
    }
    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}
