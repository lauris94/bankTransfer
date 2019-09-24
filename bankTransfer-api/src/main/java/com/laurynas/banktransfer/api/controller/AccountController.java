package com.laurynas.banktransfer.api.controller;

import com.laurynas.banktransfer.api.model.CreateAccountRequest;
import com.laurynas.banktransfer.api.model.GetAccountResponse;
import com.laurynas.banktransfer.api.service.AccountService;
import com.laurynas.banktransfer.api.service.mapper.AccountMapper;
import com.laurynas.banktransfer.jpa.model.Account;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @Get
    public List<GetAccountResponse> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return accounts.stream()
                .map(accountMapper::toGetAccountResponse)
                .collect(Collectors.toList());
    }

    @Get(uri = "/{id}")
    public GetAccountResponse getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return accountMapper.toGetAccountResponse(account);
    }

    @Post
    public HttpResponse<GetAccountResponse> createAccount(@Body @Valid CreateAccountRequest accountToCreate) {
        Account createdAccount = accountService.createAccountForUser(accountToCreate);
        return HttpResponse
                .created(accountMapper.toGetAccountResponse(createdAccount));
    }
}
