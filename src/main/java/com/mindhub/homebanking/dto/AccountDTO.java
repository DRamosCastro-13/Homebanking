package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private Long id;

    private String number;

    private AccountType type;

    private LocalDate creationDate;

    private Double balance;

    private Boolean active;

    private Set<TransactionDTO> transactions;

    public AccountDTO(Account account){
        id = account.getId();
        number = account.getNumber();
        type = account.getType();
        creationDate = account.getCreationDate();
        balance = account.getBalance();
        transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
        active = account.getActive();
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public AccountType getType() {
        return type;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public Boolean getActive(){return active;}

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", type=" + type +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                ", transactions=" + transactions +
                '}';
    }
}
