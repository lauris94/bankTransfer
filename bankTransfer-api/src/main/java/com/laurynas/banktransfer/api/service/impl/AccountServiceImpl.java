package com.laurynas.banktransfer.api.service.impl;

import com.laurynas.banktransfer.api.exception.NotFoundException;
import com.laurynas.banktransfer.api.model.CreateAccountRequest;
import com.laurynas.banktransfer.api.service.mapper.AccountMapper;
import com.laurynas.banktransfer.api.service.AccountService;
import com.laurynas.banktransfer.api.service.UserService;
import com.laurynas.banktransfer.jpa.model.Account;
import com.laurynas.banktransfer.jpa.model.User;
import com.laurynas.banktransfer.jpa.repository.AccountRepository;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class AccountServiceImpl implements AccountService {

    private final UserService userService;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    public AccountServiceImpl(UserService userService, AccountMapper accountMapper, AccountRepository accountRepository) {
        this.userService = userService;
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccountForUser(CreateAccountRequest createAccountRequest) {
        Long userId = createAccountRequest.getUserId();
        Account accountToSave;
        if (userId != null) {
            User user = userService.getUserById(userId);
            accountToSave = accountMapper.toAccountEntity(user, createAccountRequest);
        } else {
            accountToSave = accountMapper.toAccountEntity(createAccountRequest);
        }
        return accountRepository.save(accountToSave);
    }

    @Override
    public Account getAccountById(Long id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        return accountOptional
                .orElseThrow(() -> new NotFoundException(String.format("Account with id %s does not exist", id)));
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
