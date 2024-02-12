package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
//@PropertySource("classpath:.env")
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	/*@Autowired
	public PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return args -> {

			Client admin = new Client("Daniela", "Ramos", "daniela@adminanb.com", passwordEncoder.encode("098765"));
			admin.setRole(RoleType.ADMIN);

			Client renata = new Client("Renata", "Castillo", "hola@mindhub.com", passwordEncoder.encode("10205"));
			renata.setRole(RoleType.CLIENT);

			Loan mortgage = new Loan("Mortgage Loan", 500000.0, List.of(12, 24, 36, 48, 60), 0.2);
			Loan personal = new Loan("Personal Loan", 100000.0, List.of(6, 12, 24), 0.1);
			Loan auto = new Loan("Auto Finance", 300000.0, List.of(6, 12, 24, 36), 0.15);

			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(auto);

			clientRepository.save(renata);
			clientRepository.save(admin);

			Account account1 = new Account("VIN-42114463", AccountType.CHECKING, LocalDate.now(), 2000.0);
			Card card1 = new Card("RENATA CASTILLO", CardType.CREDIT, CardColor.SILVER, Utils.generateCardNumber(), Utils.generateCvv(), LocalDate.now().minusDays(2), LocalDate.now().minusYears(5));
			Card card2 = new Card("RENATA CASTILLO", CardType.DEBIT, CardColor.SILVER, Utils.generateCardNumber(), Utils.generateCvv(), LocalDate.now().plusYears(5), LocalDate.now());

			renata.addAccount(account1);
			renata.addCard(card1);
			renata.addCard(card2);

			accountRepository.save(account1);
			cardRepository.save(card1);
			cardRepository.save(card2);

		};
	}*/
}
