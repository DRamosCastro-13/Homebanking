package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.NewCardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;


    @PostMapping("/clients/current")
    public ResponseEntity<String> createCard(
            @RequestBody NewCardDTO newCard,
            Authentication authentication

    ){
        Client client = clientService.getAuthenticatedClient(authentication.getName());

        if(newCard.type().toString().isBlank() && newCard.color().toString().isBlank()){
            return new ResponseEntity<>("You must select an option to create the card", HttpStatus.FORBIDDEN);
        }

        if(newCard.type().toString().isBlank()){
            return new ResponseEntity<>("Please select the type of card you would like to choose", HttpStatus.FORBIDDEN);
        }

        if(newCard.color().toString().isBlank()){
            return new ResponseEntity<>("Please select the color of the card you would like to choose", HttpStatus.FORBIDDEN);
        }

        if(client.getCards().stream().anyMatch(card -> card.getType().equals(newCard.type()) && card.getColor().equals(newCard.color()))){
            return new ResponseEntity<>("You have reached the maximum number of " +
                    newCard.color() + " " + newCard.type() + " cards.", HttpStatus.FORBIDDEN);
        }

        String cvv;
        String cardNumber1;
        String cardNumber2;
        String cardNumber3;
        String cardNumber4;

        do {
            cardNumber1 = String.valueOf(getRandomNumber(4000, 9999));

        } while (cardService.existsByNumber(cardNumber1));

        do {
            cardNumber2 = String.valueOf(getRandomNumber(1000, 9999));

        } while (cardService.existsByNumber(cardNumber2));

        do {
            cardNumber3 = String.valueOf(getRandomNumber(1000, 9999));

        } while (cardService.existsByNumber(cardNumber3));

        do {
            cardNumber4 = String.valueOf(getRandomNumber(1000, 9999));

        } while (cardService.existsByNumber(cardNumber4));

        do {
            cvv = String.valueOf(getRandomNumber(100, 999));

        } while (cardService.existsByNumber(cvv));


        Card card = new Card(client.getFirstName().toUpperCase() + " " +
                client.getLastName().toUpperCase(), newCard.type(), newCard.color(),
                cardNumber1 + "-" + cardNumber2 + "-" + cardNumber3 + "-" + cardNumber4,
                cvv, LocalDate.now().plusYears(5), LocalDate.now());

        client.addCard(card);
        cardService.saveCard(card);

        return new ResponseEntity<>("Card created for " + client.getLastName() + ", "  +
                client.getFirstName(), HttpStatus.CREATED);

    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
