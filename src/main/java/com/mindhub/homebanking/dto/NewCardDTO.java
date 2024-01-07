package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

public record NewCardDTO(CardType type, CardColor color) {
}
