package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utils.getRandomNumber;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;


    @PostMapping("/clients/current")
    public ResponseEntity<String> createAccount(
            @RequestParam AccountType type,
            Authentication authentication
    ){
        Client client = clientService.getAuthenticatedClient(authentication.getName()); //Encontrar al dueño

        if(client.getAccounts().stream().filter(Account::getActive).toList().size()>=3){
            return new ResponseEntity<>("Maximum accounts per client reached", HttpStatus.FORBIDDEN);
        }

        String number;

        do {
            number = Utils.generateAccountNumber();
        } while (accountService.existsByNumber(number));

        Account account = new Account(number, type, LocalDate.now(), 0.0);
        client.addAccount(account);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account created for " + client.getLastName() + ", "  + client.getFirstName(), HttpStatus.CREATED);
    }


    @GetMapping("/all")
    public List<AccountDTO> getAllAccounts(){
        return accountService.getAllAccountsDTO();
    }

    @GetMapping("/{id}")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id,
                                                Authentication authentication){
        if(id == null){
            return new ResponseEntity<>("Invalid account ID", HttpStatus.NOT_FOUND);
        }

        Client client = clientService.getAuthenticatedClient(authentication.getName());
        Account account = accountService.findByClientAndId(client, id);

        if(account == null){
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        if(account.getBalance() != 0){
            return new ResponseEntity<>("You cannot delete an account that has a remaining balance", HttpStatus.CONFLICT);
        }

        if (!account.getClient().equals(client)){
            return new ResponseEntity<>("Account does not match owner", HttpStatus.FORBIDDEN);
        }

        if(client.getAccounts().stream().filter(Account::getActive).toList().size() == 1){
            return new ResponseEntity<>("You cannot delete your only account", HttpStatus.FORBIDDEN);
        }

        account.setActive(false);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account deleted succesfully", HttpStatus.OK);
    }

}
