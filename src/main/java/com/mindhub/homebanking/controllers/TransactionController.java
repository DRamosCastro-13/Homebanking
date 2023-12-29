package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @PostMapping("/clients/current")
    public ResponseEntity<String> newTransaction(
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String originAccount,
            @RequestParam String targetAccount,
            Authentication authentication
    ){
        Client client = clientRepository.findByEmail(authentication.getName());

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
            return new ResponseEntity<>("You cannot transfer money to your own account, please review the information", HttpStatus.FORBIDDEN);
        }

        if(!accountRepository.existsByNumber(originAccount)){
            return new ResponseEntity<>("Unable to find Origin Account, please review the information", HttpStatus.FORBIDDEN);
        }

        if(!accountRepository.existsByNumber(targetAccount)){
            return new ResponseEntity<>("Unable to find Target Account, please review the information", HttpStatus.FORBIDDEN);
        }

        if(!accountRepository.existsByNumberAndClient(originAccount, client)){
            return new ResponseEntity<>("Unable to complete the transaction, please review the account information", HttpStatus.FORBIDDEN);
        }


    }

}
