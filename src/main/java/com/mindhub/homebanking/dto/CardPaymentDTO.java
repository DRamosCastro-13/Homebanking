package com.mindhub.homebanking.dto;

import java.time.LocalDate;

public record CardPaymentDTO(String number, String cvv, Double amount, String description) {
}
