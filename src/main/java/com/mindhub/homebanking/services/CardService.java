package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

public interface CardService {

    boolean existsByNumber(String number);

    void saveCard(Card card);

    Card findByClientAndId(Client client, Long id);
}
