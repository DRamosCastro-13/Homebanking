package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {
    boolean existsByNumber(String number);

    List<Account> getAllAccounts();

    List<AccountDTO> getAllAccountsDTO();

    Account findByClientAndId(Client client, Long id);

    Account findByNumberAndClient(String number, Client client);

    Account findByNumber(String number);

    void saveAccount(Account account);
}
