package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional //Indica que el servlet está bajo ACID
    @PostMapping("/clients/current")
    public ResponseEntity<String> newTransaction(
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String originAccount,
            @RequestParam String targetAccount,
            Authentication authentication
    ){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account originTransaction = accountRepository.findByNumberAndClient(originAccount, client);
        Account targetTransaction = accountRepository.findByNumber(targetAccount);

        if(String.valueOf(amount).isBlank() && description.isBlank() && originAccount.isBlank() && targetAccount.isBlank()){
            return new ResponseEntity<>("You must fill out the form to complete the transaction", HttpStatus.FORBIDDEN);
        }

        if(String.valueOf(amount).isBlank()){
            return new ResponseEntity<>("Specify the Amount you will transfer", HttpStatus.FORBIDDEN);
        }

        if(description.isBlank()){
            return new ResponseEntity<>("You must include the Description for the transaction", HttpStatus.FORBIDDEN);
        }

        if(originAccount.isBlank()){
            return new ResponseEntity<>("You must specify the Origin Account", HttpStatus.FORBIDDEN);
        }

        if(targetAccount.isBlank()){
            return new ResponseEntity<>("You must specify the Target Account Number", HttpStatus.FORBIDDEN);
        }

        if(originAccount.equals(targetAccount)){
            return new ResponseEntity<>("You cannot transfer money to the same account, please review the information", HttpStatus.FORBIDDEN);
        }

        if(amount < 1){
            return new ResponseEntity<>("You cannot transfer this amount, try again", HttpStatus.FORBIDDEN);
        }

        if(originTransaction == null){
            return new ResponseEntity<>("Unable to find Origin Account, please review the information", HttpStatus.FORBIDDEN);
        }

        if(targetTransaction == null ){
            return new ResponseEntity<>("Unable to find Target Account, please review the information", HttpStatus.FORBIDDEN);
        }


        if(originTransaction.getBalance() < amount){
            return new ResponseEntity<>("Insufficient funds to complete the transaction", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, "DEBIT. " + originAccount
                + ". " + description.substring(0,1).toUpperCase() + description.substring(1).toLowerCase(),
                -amount, LocalDate.now());
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, "CREDIT. " + targetAccount +
                ". " + description.substring(0,1).toUpperCase() + description.substring(1).toLowerCase(),
                amount, LocalDate.now());

        originTransaction.setBalance(originTransaction.getBalance() - amount);
        targetTransaction.setBalance(targetTransaction.getBalance() + amount);

        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);

        accountRepository.save(originTransaction);
        accountRepository.save(targetTransaction);

        return new ResponseEntity<>("Success", HttpStatus.OK);

    }

}
