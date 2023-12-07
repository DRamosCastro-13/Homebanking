package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
		return args -> {

			Client melba = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepository.save(melba);
			System.out.println(melba);



			Account account001 = new Account("VIN-001", LocalDate.now(),5000.0);
			Account account002 = new Account("VIN-002", LocalDate.now().plusDays(1),7500.0);

			melba.addAccount(account001);
			melba.addAccount(account002); //Añade las cuentas al cliente

			accountRepository.save(account001);
			accountRepository.save(account002); //Guarda las cuentas en la base de datos



			System.out.println(account002);
		}; //clean gradle
	}

}
