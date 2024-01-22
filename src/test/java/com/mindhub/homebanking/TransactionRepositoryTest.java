package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    /*@Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions, is(not(empty())));
    }*/

    @Test
    public void hasType() {
        List<Transaction> transactions = transactionRepository.findAll();

        assertThat("Every transaction must have a non-null type", transactions, everyItem(hasProperty("type", is(notNullValue()))));
    }

    /*@Test
    public void hasDescription(){
        List<Transaction> transactions = transactionRepository.findAll();

        assertThat("Every transaction must have a non-null description", transactions, hasItem(hasProperty("description", is(not(empty())))));
    }*/



}
