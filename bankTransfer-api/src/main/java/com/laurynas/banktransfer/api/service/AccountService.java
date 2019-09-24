package com.laurynas.banktransfer.api.service;

import com.laurynas.banktransfer.api.model.CreateAccountRequest;
import com.laurynas.banktransfer.jpa.model.Account;

import java.util.List;

public interface AccountService {

    Account createAccountForUser(CreateAccountRequest createAccountRequest);

    Account getAccountById(Long id);

    List<Account> getAllAccounts();
}
