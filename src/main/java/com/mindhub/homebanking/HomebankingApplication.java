package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {

			Client melba = new Client("Melba", "Morel", "melba@mindhub.com", "123543");
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

			Transaction transaction1 = new Transaction(TransactionType.DEBIT, "Pizza-Hut",-20.0,LocalDate.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, "Sephora Inc.", -150.0,LocalDate.now());
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, "PayPal", 200.0, LocalDate.now());
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, "Chick-fil-A", -40.39, LocalDate.now());
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, "Bank Wire Transfer", 320.0, LocalDate.now());

			account001.addTransaction(transaction1);
			account001.addTransaction(transaction2);
			account001.addTransaction(transaction3);
			account002.addTransaction(transaction4);
			account002.addTransaction(transaction5);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);

			Loan mortgage = new Loan("Mortgage Loan",500000.0, List.of(12,24,36,48,60));
			Loan personal = new Loan("Personal Loan",100000.0,List.of(6,12,24));
			Loan auto = new Loan("Auto Finance", 300000.0,List.of(6,12,24,36));

			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(auto);

			ClientLoan mortgageMelba = new ClientLoan(400000.0, 60);
			ClientLoan personalMelba = new ClientLoan(50000.0,12);

			melba.addClientLoan(mortgageMelba);
			mortgage.addClientLoan(mortgageMelba);

			melba.addClientLoan(personalMelba);
			personal.addClientLoan(personalMelba);


			clientLoanRepository.save(mortgageMelba);
			clientLoanRepository.save(personalMelba);

			Card goldMelba =  new Card(melba.getFirstName() + " "+ melba.getLastName(),CardType.DEBIT,CardColor.GOLD, "4544-5900-0223-7173","741",LocalDate.now().plusYears(5),LocalDate.now());
			Card titaniumMelba = new Card(melba.getFirstName() + " "+ melba.getLastName(), CardType.CREDIT,CardColor.TITANIUM, "5548-9373-0029-1275", "420",LocalDate.now().plusYears(5),LocalDate.now());

			melba.addCard(goldMelba);
			melba.addCard(titaniumMelba);


			cardRepository.save(goldMelba);
			cardRepository.save(titaniumMelba);

		}; //clean gradle
	}

}
