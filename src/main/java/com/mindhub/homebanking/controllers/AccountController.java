package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
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
    private AccountService accountService;

    @Autowired
    private ClientService clientService;


    @PostMapping("/clients/current")
    public ResponseEntity<String> createAccount(
            Authentication authentication
    ){
        Client client = clientService.getAuthenticatedClient(authentication.getName()); //Encontrar al dueño

        if(client.getAccounts().size()>=3){
            return new ResponseEntity<>("Maximum accounts per client reached", HttpStatus.FORBIDDEN);
        }

        String number;

        do {
            number = "VIN-" + getRandomNumber(100000, 99999999);
        } while (accountService.existsByNumber(number));

        Account account = new Account(number, LocalDate.now(), 0.0);
        client.addAccount(account);
       accountService.saveAccount(account);

        return new ResponseEntity<>("Account created for " + client.getLastName() + ", "  + client.getFirstName(), HttpStatus.CREATED);
    }


    @RequestMapping("/all")
    public List<AccountDTO> getAllAccounts(){
        return accountService.getAllAccountsDTO();
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Object> getOneAccount(@PathVariable Long id,
                                    Authentication authentication){
        Client client = clientService.getAuthenticatedClient(authentication.getName());

        Account account = accountService.findByClientAndId(client, id);

        if(account != null){
            return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Account not found", HttpStatus.BAD_REQUEST);
        }

    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
