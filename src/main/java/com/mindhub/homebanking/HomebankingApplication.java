package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	@Autowired
	public PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {

			Client admin = new Client("Daniela","Ramos","daniela@mindhub.com", passwordEncoder.encode("098765"));
			admin.setRole(RoleType.ADMIN);

			Client renata = new Client("Renata","Castillo", "hola@mindhub.com", passwordEncoder.encode("10205"));
			renata.setRole(RoleType.CLIENT);


			clientRepository.save(renata);
			clientRepository.save(admin);

			Account account1 = new Account("VIN-42114463", LocalDate.now(), 2000.0);
			renata.addAccount(account1);

			accountRepository.save(account1);

		}; //clean gradle
	}

}
