package com.mindhub.homebanking.dto;

import java.util.List;

public record LoanApplicationDTO(String name, Double amount, Integer payments, String targetAccount) {
}
