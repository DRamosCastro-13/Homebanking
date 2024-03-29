package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.dto.LoanPaymentDTO;
import com.mindhub.homebanking.dto.NewLoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    @GetMapping("/availableLoans")
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

        if(loanApplication.name().isBlank() && loanApplication.amount().toString().isBlank()
        && loanApplication.payments().toString().isBlank() && loanApplication.targetAccount().isBlank()){
            return new ResponseEntity<>("You must fill out the form", HttpStatus.FORBIDDEN);
        }

        if(loanApplication.name().isBlank()){
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

        if(loanApplication.amount() < 10000){
            return new ResponseEntity<>("The minimum amount for a loan is $10,000", HttpStatus.FORBIDDEN);
        }

        if(loanApplication.payments() == 0){
            return new ResponseEntity<>("You must select how you wish to defer the debt", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanService.findByName(loanApplication.name());

        if(loan == null){
            return new ResponseEntity<>("The loan you are trying to request does not exist", HttpStatus.FORBIDDEN);
        }

        if(loanApplication.amount() > loanService.getLoanDTOByName(loanApplication.name()).getMaxAmount()){
            return new ResponseEntity<>("You are exceeding the maximum amount for this loan", HttpStatus.FORBIDDEN);
        }

        if(!loanService.getLoanDTOByName(loanApplication.name()).getPayments().contains(loanApplication.payments())){
            return new ResponseEntity<>("Invalid number of payments to defer", HttpStatus.FORBIDDEN);
        }

        Account targetAccount = accountService.findByNumberAndClient(loanApplication.targetAccount(), client);

        if(targetAccount == null){
            return new ResponseEntity<>("Unable to locate the account to transfer funds", HttpStatus.FORBIDDEN);
        }

        if(client.getClientLoans().stream().anyMatch(clientLoan -> clientLoan.getLoan().getName().equals(loanApplication.name()))){
            return new ResponseEntity<>("You have reached the maximum number of loans of this type ", HttpStatus.FORBIDDEN);
        }

        ClientLoan newLoan = new ClientLoan((loanApplication.amount() * loan.getInterest()) + loanApplication.amount(), loanApplication.payments(), 0);

        Transaction credit = new Transaction(TransactionType.CREDIT, loanService.getLoanDTOByName(loanApplication.name()).getName() +
                " - APPROVED", loanApplication.amount(), LocalDate.now(), targetAccount.getBalance() + loanApplication.amount());


        targetAccount.setBalance(targetAccount.getBalance() + loanApplication.amount());
        targetAccount.addTransaction(credit);
        client.addClientLoan(newLoan);
        loan.addClientLoan(newLoan);

        clientLoanService.saveClientLoan(newLoan);
        transactionService.saveTransaction(credit);
        accountService.saveAccount(targetAccount);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);

    }

    @Transactional
    @PostMapping("/payment")
    public ResponseEntity<String> makeALoanPayment(
            @RequestBody LoanPaymentDTO loanPayment,
            Authentication authentication
    ){
        Client client = clientService.getAuthenticatedClient(authentication.getName());
        ClientLoan clientLoan = clientLoanService.findByClientAndId(client, loanPayment.id());
        Account account = accountService.findByNumber(loanPayment.account());

        if (client == null) {
            return new ResponseEntity<>("Unable to verify client", HttpStatus.FORBIDDEN);
        }

        if (clientLoan == null || !clientLoan.getClient().equals(client)) {
            return new ResponseEntity<>("Invalid loan or client, please review the information", HttpStatus.FORBIDDEN);
        }

        if (loanPayment.amount().toString().isBlank() || loanPayment.account().isBlank() || loanPayment.id().toString().isBlank()) {
            return new ResponseEntity<>("You must fill out the form to make the payment", HttpStatus.FORBIDDEN);
        }

        if (loanPayment.amount() < 1000) {
            return new ResponseEntity<>("The minimum amount for a payment is $1,000", HttpStatus.FORBIDDEN);
        }

        if (clientLoan.getAmount() < loanPayment.amount()) {
            return new ResponseEntity<>("The remaining loan amount is less than the payment amount.", HttpStatus.FORBIDDEN);
        }

        if (account.getBalance() - loanPayment.amount() < 0) {
            return new ResponseEntity<>("Your account has insufficient funds for this transaction", HttpStatus.FORBIDDEN);
        }


        Transaction transaction = new Transaction(
                TransactionType.DEBIT,
                clientLoan.getLoan().getName() + " payment",
                loanPayment.amount(),
                LocalDate.now(),
                account.getBalance() - loanPayment.amount()
        );

        clientLoan.setPaymentsMade(clientLoan.getPaymentsMade() + 1);
        clientLoan.setAmount(clientLoan.getAmount() - loanPayment.amount());
        account.setBalance(account.getBalance() - loanPayment.amount());
        account.addTransaction(transaction);

        transactionService.saveTransaction(transaction);
        accountService.saveAccount(account);
        clientLoanService.saveClientLoan(clientLoan);

        return new ResponseEntity<>("Payment Successful", HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createALoan(
            @RequestBody NewLoanDTO newLoan,
            Authentication authentication
            ){
        Client client = clientService.getAuthenticatedClient(authentication.getName());

        if(client == null){
            return new ResponseEntity<>("Unable to verify Admin", HttpStatus.FORBIDDEN);
        }

        if(client.getRole() != RoleType.ADMIN){
            return new ResponseEntity<>("You don't have enough credentials to make this request", HttpStatus.FORBIDDEN);
        }

        if(newLoan.name().isBlank() || newLoan.interest().toString().isBlank() || newLoan.maxAmount().toString().isBlank() || newLoan.payments().toString().isBlank()){
            return new ResponseEntity<>("You must fill out the form and complete all fields", HttpStatus.FORBIDDEN);
        }

        if(newLoan.maxAmount() < 10000){
            return new ResponseEntity<>("The minimum max amount to create a loan is $10,000.00", HttpStatus.FORBIDDEN);
        }

        if(newLoan.maxAmount() > 1000000){
            return new ResponseEntity<>("The maximum amount for a new loan can not be more than $1,000,000.0", HttpStatus.FORBIDDEN);
        }

        if(newLoan.interest() < 0.1){
            return new ResponseEntity<>("The minimum interest rate is 10%", HttpStatus.FORBIDDEN);
        }

        if(newLoan.interest() > 0.3){
            return new ResponseEntity<>("The maximum interest rate is 30%", HttpStatus.FORBIDDEN);
        }

        Boolean loan = loanService.existsByName(newLoan.name());

        if(loan){
            return new ResponseEntity<>("This loan already exists", HttpStatus.FORBIDDEN);
        }

        Loan newLoanCreation = new Loan(newLoan.name(), newLoan.maxAmount(),
                newLoan.payments(), newLoan.interest());

        loanService.saveLoan(newLoanCreation);

        return new ResponseEntity<>(newLoanCreation.getName() + " Created Successfully", HttpStatus.CREATED);

    }

}
