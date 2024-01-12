package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest //se usa esta anotación ya que se va a hacer el testing sobre un JPA Repository
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //para hacer una autoconfiguración
//para que la base de datos esté abierta al hacer el test
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;


    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));

    }

    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat("Every loan must have a type(name)", loans, hasItem(hasProperty("name", is("Personal Loan"))));

    }

}


