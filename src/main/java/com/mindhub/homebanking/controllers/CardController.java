package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<String> createCard(
            @RequestParam CardType type,
            @RequestParam CardColor color,
            Authentication authentication

    ){
        Client client = clientRepository.findByEmail(authentication.getName());

        if(client.getCards().stream().anyMatch(card -> card.getType().equals(type) && card.getColor().equals(color))){
            return new ResponseEntity<>("You have reached the maximum number of " + color + " " + type + " cards.", HttpStatus.FORBIDDEN);
        }

        String cvv;
        String cardNumber;

    }

}
