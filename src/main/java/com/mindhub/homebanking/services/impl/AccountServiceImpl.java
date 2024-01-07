package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean existsByNumber(String number) {
        return accountRepository.existsByNumber(number);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<AccountDTO> getAllAccountsDTO() {
        return getAllAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @Override
    public Account findByClientAndId(Client client, Long id) {
        return accountRepository.findByClientAndId(client, id);
    }

    @Override
    public Account findByNumberAndClient(String number, Client client) {
        return accountRepository.findByNumberAndClient(number, client);
    }

    @Override
    public Account findByNumer(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}
