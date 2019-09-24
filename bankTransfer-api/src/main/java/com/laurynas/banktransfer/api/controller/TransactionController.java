package com.laurynas.banktransfer.api.controller;

import com.laurynas.banktransfer.api.exception.BadRequestException;
import com.laurynas.banktransfer.api.model.GetTransactionResponse;
import com.laurynas.banktransfer.api.model.CreateTransactionRequest;
import com.laurynas.banktransfer.api.service.AccountService;
import com.laurynas.banktransfer.api.service.TransactionService;
import com.laurynas.banktransfer.api.service.mapper.TransactionMapper;
import com.laurynas.banktransfer.jpa.model.Account;
import com.laurynas.banktransfer.jpa.model.Currency;
import com.laurynas.banktransfer.jpa.model.Transaction;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import java.math.BigDecimal;

@Controller("/api/transactions")
public class TransactionController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public TransactionController(AccountService accountService, TransactionService transactionService,
                                 TransactionMapper transactionMapper) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @Post
    public GetTransactionResponse createTransaction(CreateTransactionRequest transactionToCreate) {
        Account sender = accountService.getAccountById(transactionToCreate.getSenderAccountId());
        Account receiver = accountService.getAccountById(transactionToCreate.getReceiverAccountId());

        validateCurrencies(transactionToCreate.getCurrency(), sender, receiver);
        validateBalances(transactionToCreate.getAmount(), sender);
        validateAmount(transactionToCreate.getAmount());

        Transaction createdTransaction = transactionService.performMoneyTransfer(sender, receiver, transactionToCreate.getAmount(), transactionToCreate.getCurrency());
        return transactionMapper.toGetTransactionResponse(createdTransaction);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount should be greater than 0");
        }
    }

    private void validateBalances(BigDecimal amount, Account sender) {
        if (amount.compareTo(sender.getBalance()) > 0) {
            throw new BadRequestException(String.format("Insufficient funds in sender's account. Sender id %s", sender.getId()));
        }
    }

    private void validateCurrencies(Currency transactionCurrency, Account sender, Account receiver) {
        if (transactionCurrency != sender.getCurrency() || transactionCurrency != receiver.getCurrency()) {
            throw new BadRequestException(String.format("Currencies doesn't match! Transaction: %s, Sender: %s, Receiver: %s",
                    transactionCurrency, sender.getCurrency(), receiver.getCurrency()));
        }
    }
}
