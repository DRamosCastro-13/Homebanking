package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

public interface CardService {

    boolean existsByNumber(String number);

    void saveCard(Card card);
}
