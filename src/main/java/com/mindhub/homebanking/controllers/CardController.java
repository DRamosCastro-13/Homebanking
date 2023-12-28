package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @PostMapping
    public ResponseEntity<String> createCard(
            @RequestParam CardType type,
            @RequestParam CardColor color,
            Authentication authentication

    ){
        Client client = clientRepository.findByEmail(authentication.getName());

        if(client.getCards().stream().anyMatch(card -> card.getType().equals(type) && card.getColor().equals(color))){
            return new ResponseEntity<>("You have reached the maximum number of " +
                    color + " " + type + " cards.", HttpStatus.FORBIDDEN);
        }

        String cvv;
        String cardNumber1;
        String cardNumber2;
        String cardNumber3;
        String cardNumber4;

        do {
            cardNumber1 = String.valueOf(getRandomNumber(4000, 9999));

        } while (cardRepository.existsByNumber(cardNumber1));

        do {
            cardNumber2 = String.valueOf(getRandomNumber(1000, 9999));

        } while (cardRepository.existsByNumber(cardNumber2));

        do {
            cardNumber3 = String.valueOf(getRandomNumber(1000, 9999));

        } while (cardRepository.existsByNumber(cardNumber3));

        do {
            cardNumber4 = String.valueOf(getRandomNumber(1000, 9999));

        } while (cardRepository.existsByNumber(cardNumber4));

        do {
            cvv = String.valueOf(getRandomNumber(100, 999));

        } while (cardRepository.existsByNumber(cvv));


        Card card = new Card(client.getFirstName().toUpperCase() + " " +
                client.getLastName().toUpperCase(), CardType.valueOf(String.valueOf(type)), CardColor.valueOf(String.valueOf(color)),
                cardNumber1 + "-" + cardNumber2 + "-" + cardNumber3 + "-" + cardNumber4,
                cvv, LocalDate.now().plusYears(5), LocalDate.now());

        client.addCard(card);
        cardRepository.save(card);

        return new ResponseEntity<>("Card created for " + client.getLastName() + ", "  +
                client.getFirstName(), HttpStatus.CREATED);

    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
