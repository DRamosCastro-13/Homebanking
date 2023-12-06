package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository){
		return args -> {
			Client client = new Client();


			Client daniela = new Client("Daniela", "Ramos", "danielarc@gmail.com");
			clientRepository.save(daniela);
			System.out.println(daniela);

			Client melba = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepository.save(melba);
			System.out.println(melba);
		}; //clean gradle
	}

}
