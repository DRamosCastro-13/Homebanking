package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void existsAccount(){
        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts, is(not(empty())));
    }

    @Test
    public void creationDate(){
        List<Account> accounts = accountRepository.findAll();

        assertThat("Every account must have a creation date", accounts, everyItem(hasProperty("creationDate", notNullValue())));
    }

    @Test
    public void validBalance(){
        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts, everyItem(hasProperty("balance", greaterThanOrEqualTo(0.0))));
    }

}
