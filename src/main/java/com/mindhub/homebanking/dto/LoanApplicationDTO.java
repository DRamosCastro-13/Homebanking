package com.mindhub.homebanking.dto;

import java.util.List;

public record LoanApplicationDTO(Long id, Double amount, Integer payments, String targetAccount) {
}
