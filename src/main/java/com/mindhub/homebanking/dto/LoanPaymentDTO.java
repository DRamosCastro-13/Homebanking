package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;

public record LoanPaymentDTO(Long id, Double amount, String account) {
}
