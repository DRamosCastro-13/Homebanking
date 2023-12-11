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
//			Client clara = new Client("Clara", "Rodriguez", "crodriguez@aol.com");
//			clientRepository.save(clara);


			Account account001 = new Account("VIN-001", LocalDate.now(),5000.0);
			Account account002 = new Account("VIN-002", LocalDate.now().plusDays(1),7500.0);
			//Account account003 = new Account("VIN-003", LocalDate.now().plusDays(2), 7750.0);

			melba.addAccount(account001);
			melba.addAccount(account002); //Añade las cuentas al cliente
			//clara.addAccount(account003);

			accountRepository.save(account001);
			accountRepository.save(account002); //Guarda las cuentas en la base de datos
			//accountRepository.save(account003);


			System.out.println(account002);
		}; //clean gradle
	}

}
