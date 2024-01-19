package com.mindhub.homebanking.dto;

import java.util.List;

public record NewLoanDTO(String name, Double maxAmount, List<Integer> payments, Double interest) {
}
