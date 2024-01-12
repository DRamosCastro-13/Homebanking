package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void existsClient(){
        List<Client> clients = clientRepository.findAll();

        assertThat(clients, is(not(empty())));
    }

    @Test
    public void hasValidId(){
        List<Client> clients = clientRepository.findAll();

        assertThat("Every client should have a non-null id", clients, hasItem(hasProperty("id", notNullValue())));
        assertThat("Every client's id should be greater than 0", clients, everyItem(hasProperty("id", greaterThan(Long.valueOf(0)))));
    }

    @Test
    public void validEmail(){
        List<Client> clients = clientRepository.findAll();

        assertThat("All clients should have an email containing '@'",clients, everyItem(hasProperty("email", containsString("@"))));
    }


}
