package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.NewTransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Transactional //Indica que el servlet está bajo ACID
    @PostMapping("")
    public ResponseEntity<String> newTransaction(
            @RequestBody NewTransactionDTO newTransaction,
            Authentication authentication
    ){
        Client client = clientService.getAuthenticatedClient(authentication.getName());

        if(String.valueOf(newTransaction.amount()).isBlank() && newTransaction.description().isBlank() && newTransaction.originAccount().isBlank() && newTransaction.targetAccount().isBlank()){
            return new ResponseEntity<>("You must fill out the form to complete the transaction", HttpStatus.FORBIDDEN);
        }

        if(String.valueOf(newTransaction.amount()).isBlank()){
            return new ResponseEntity<>("Specify the Amount you will transfer", HttpStatus.FORBIDDEN);
        }

        if(newTransaction.description().isBlank()){
            return new ResponseEntity<>("You must include the Description for the transaction", HttpStatus.FORBIDDEN);
        }

        if(newTransaction.originAccount().isBlank()){
            return new ResponseEntity<>("You must specify the Origin Account", HttpStatus.FORBIDDEN);
        }

        if(newTransaction.targetAccount().isBlank()){
            return new ResponseEntity<>("You must specify the Target Account Number", HttpStatus.FORBIDDEN);
        }

        if(newTransaction.originAccount().equals(newTransaction.targetAccount())){
            return new ResponseEntity<>("You cannot transfer money to the same account, please review the information", HttpStatus.FORBIDDEN);
        }

        if(newTransaction.amount() < 1){
            return new ResponseEntity<>("You cannot transfer this amount, try again", HttpStatus.FORBIDDEN);
        }

        Account originTransactionAcc = accountService.findByNumberAndClient(newTransaction.originAccount(), client);
        Account targetTransactionAcc = accountService.findByNumber(newTransaction.targetAccount());

        if(originTransactionAcc == null){
            return new ResponseEntity<>("Unable to find Origin Account, please review the information", HttpStatus.FORBIDDEN);
        }

        if(targetTransactionAcc == null ){
            return new ResponseEntity<>("Unable to find Target Account, please review the information", HttpStatus.FORBIDDEN);
        }


        if(originTransactionAcc.getBalance() < newTransaction.amount()){
            return new ResponseEntity<>("Insufficient funds to complete the transaction", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, "DEBIT. " + newTransaction.originAccount()
                + ". " + newTransaction.description().substring(0,1).toUpperCase() + newTransaction.description().substring(1).toLowerCase(),
                -newTransaction.amount(), LocalDate.now(), originTransactionAcc.getBalance() - newTransaction.amount());
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, "CREDIT. " + newTransaction.targetAccount() +
                ". " + newTransaction.description().substring(0,1).toUpperCase() + newTransaction.description().substring(1).toLowerCase(),
                newTransaction.amount(), LocalDate.now(), targetTransactionAcc.getBalance() + newTransaction.amount());

        originTransactionAcc.setBalance(originTransactionAcc.getBalance() - newTransaction.amount());
        targetTransactionAcc.setBalance(targetTransactionAcc.getBalance() + newTransaction.amount());

        originTransactionAcc.addTransaction(transactionDebit);
        targetTransactionAcc.addTransaction(transactionCredit);

        transactionService.saveTransaction(transactionDebit);
        transactionService.saveTransaction(transactionCredit);

        accountService.saveAccount(originTransactionAcc);
        accountService.saveAccount(targetTransactionAcc); //save update

        return new ResponseEntity<>("Success", HttpStatus.OK);

    }

}
