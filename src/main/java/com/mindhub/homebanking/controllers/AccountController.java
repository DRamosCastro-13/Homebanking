package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<String> createAccount(
            @RequestParam String creationDate,
            @RequestParam Double balance,
            Authentication authentication
    ){
        Client client = clientRepository.findByEmail(authentication.getName());

        if(client.getAccounts().size()>=3){
            return new ResponseEntity<>("Max accounts reached", HttpStatus.FORBIDDEN);
        }

        String number;

        do {
            number = "VIN-" + getRandomNumber(100000, 99999999);
        } while (accountRepository.existsByNumber(number));

        Account account = new Account(number, LocalDate.now(), 0.0);
        client.addAccount(account);
        accountRepository.save(account);

        return new ResponseEntity<>("Account created for " + client.getLastName() + ", "  + client.getFirstName(), HttpStatus.CREATED);
    }

    @RequestMapping("/all")
    public Set<AccountDTO> getAllAccounts(){
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toSet());
    }

    @RequestMapping("/{id}")
    public AccountDTO getOneAccount(@PathVariable Long id){
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
