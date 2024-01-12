package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void existsCard(){
        List<Card> cards = cardRepository.findAll();

        assertThat(cards, is(not(empty())));
    }

    @Test
    public void validCardNumber(){
        List<Card> cards = cardRepository.findAll();

        assertThat("Every card must have a non-null card number", cards, hasItem(hasProperty("number", is(notNullValue()))));
        assertThat("Every card's number should have a length of 19", cards, everyItem(hasProperty("number", hasLength(19))));
    }
}
