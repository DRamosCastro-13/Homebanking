package com.mindhub.homebanking.dto;

public record NewTransactionDTO(Double amount, String description, String originAccount, String targetAccount) {
}
