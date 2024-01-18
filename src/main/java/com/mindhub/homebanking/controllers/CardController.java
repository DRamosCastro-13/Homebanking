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
import com.mindhub.homebanking.utils.Utils;
import org.hibernate.annotations.SQLDelete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.Query;
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

        if(client.getCards().stream().filter(card -> !card.getDeleted()).anyMatch(card -> card.getType().equals(newCard.type()) && card.getColor().equals(newCard.color()))){
            return new ResponseEntity<>("You have reached the maximum number of " +
                    newCard.color() + " " + newCard.type() + " cards.", HttpStatus.FORBIDDEN);
        }


        String cvv;
        String cardNumber;

        do {
            cardNumber = Utils.generateCardNumber();

        } while (cardService.existsByNumber(cardNumber));

        do {
            cvv = Utils.generateCvv();

        } while (cardService.existsByNumber(cvv));


        Card card = new Card(client.getFirstName().toUpperCase() + " " +
                client.getLastName().toUpperCase(), newCard.type(), newCard.color(),
                cardNumber, cvv, LocalDate.now().plusYears(5), LocalDate.now());

        client.addCard(card);
        cardService.saveCard(card);

        return new ResponseEntity<>("Card created for " + client.getLastName() + ", "  +
                client.getFirstName(), HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable Long id,
                                             Authentication authentication){

        if(id == null){
            return new ResponseEntity<>("Invalid Card ID", HttpStatus.NOT_FOUND);
        }

        Client client = clientService.getAuthenticatedClient(authentication.getName());
        Card card = cardService.findByClientAndId(client, id);

        if(card == null){
            return new ResponseEntity<>("Card not found", HttpStatus.NOT_FOUND);
        }

        if(!card.getClient().equals(client)){
            return new ResponseEntity<>("Card does not match owner", HttpStatus.FORBIDDEN);
        }

        card.setDeleted(true);
        cardService.saveCard(card);

        return new ResponseEntity<>("Card deleted successfully", HttpStatus.OK);

    }

}
