package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private Long id;
    private String cardholder,number,cvv;
    private CardType type;
    private CardColor color;
    private LocalDate thruDate;
    private LocalDate fromDate;
    private Boolean deleted;
    public CardDTO(Card card){
        id = card.getId();
        cardholder = card.getCardholder();
        number = card.getNumber();
        cvv = card.getCvv();
        type = card.getType();
        color = card.getColor();
        thruDate = card.getThruDate();
        fromDate = card.getFromDate();
        deleted = card.getDeleted();
    }

    public Long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }
}
