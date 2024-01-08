package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping ("/api/loans")
public class LoanController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;


    @GetMapping
    public ResponseEntity<?> availableLoans(
            Authentication authentication
    ) {
        Client client = clientService.getAuthenticatedClient(authentication.getName());

        if(client != null){
            return new ResponseEntity<>(loanService.getAllLoansDTO(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Unable to verify client, try again.", HttpStatus.FORBIDDEN);
        }
    }
    @Transactional
    @PostMapping
    public ResponseEntity<String> newLoanApplication(
            @RequestBody LoanApplicationDTO loanApplication,
            Authentication authentication
            ){
        Client client = clientService.getAuthenticatedClient(authentication.getName());

        if(loanApplication.id().toString().isBlank() && loanApplication.amount().toString().isBlank()
        && loanApplication.payments().toString().isBlank() && loanApplication.targetAccount().isBlank()){
            return new ResponseEntity<>("You must fill out the form", HttpStatus.FORBIDDEN);
        }

        if(loanApplication.id().toString().isBlank()){
            return new ResponseEntity<>("You must select the type of loan you wish to request", HttpStatus.FORBIDDEN);
        }

        if(loanApplication.amount().toString().isBlank()){
            return new ResponseEntity<>("You must specify the amount you wish to request", HttpStatus.FORBIDDEN);
        }

        if(loanApplication.payments().toString().isBlank()){
            return new ResponseEntity<>("You must select how you wish to defer the debt", HttpStatus.FORBIDDEN);
        }

        if(loanApplication.targetAccount().isBlank()){
            return new ResponseEntity<>("You must specify the account number to transfer the funds", HttpStatus.FORBIDDEN);
        }

        if(loanApplication.amount() <= 0.0){
            return new ResponseEntity<>("Invalid amount, please verify the information", HttpStatus.FORBIDDEN);
        }

        if(loanApplication.payments() == 0){
            return new ResponseEntity<>("You must select how you wish to defer the debt", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanService.findById(loanApplication.id());

        if(loan == null){
            return new ResponseEntity<>("The loan you are trying to request does not exist", HttpStatus.FORBIDDEN);
        }

        if(loanApplication.amount() > loanService.getLoanDTOById(loanApplication.id()).getMaxAmount()){
            return new ResponseEntity<>("You are exceeding the maximum amount for this loan", HttpStatus.FORBIDDEN);
        }

        if(!loanService.getLoanDTOById(loanApplication.id()).getPayments().contains(loanApplication.payments())){
            return new ResponseEntity<>("Invalid number of payments to defer", HttpStatus.FORBIDDEN);
        }

        Account targetAccount = accountService.findByNumberAndClient(loanApplication.targetAccount(), client);

        if(targetAccount == null){
            return new ResponseEntity<>("Unable to locate the account to transfer funds", HttpStatus.FORBIDDEN);
        }

        ClientLoan newLoan = new ClientLoan(loanApplication.amount() * 1.2, loanApplication.payments());

        Transaction credit = new Transaction(TransactionType.CREDIT, loanService.getLoanDTOById(loanApplication.id()).getName() +
                " - LOAN APPROVED", loanApplication.amount(), LocalDate.now());


        targetAccount.setBalance(targetAccount.getBalance() + newLoan.getAmount());
        targetAccount.addTransaction(credit);
        client.addClientLoan(newLoan);
        loan.addClientLoan(newLoan);

        clientLoanService.saveClientLoan(newLoan);
        transactionService.saveTransaction(credit);
        accountService.saveAccount(targetAccount);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);

    }

}
