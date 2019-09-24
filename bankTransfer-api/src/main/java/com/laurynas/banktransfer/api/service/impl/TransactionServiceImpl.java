package com.laurynas.banktransfer.api.service.impl;

import com.laurynas.banktransfer.api.service.TransactionService;
import com.laurynas.banktransfer.jpa.model.Account;
import com.laurynas.banktransfer.jpa.model.Currency;
import com.laurynas.banktransfer.jpa.model.Transaction;
import com.laurynas.banktransfer.jpa.repository.AccountRepository;
import com.laurynas.banktransfer.jpa.repository.TransactionRepository;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Singleton
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public Transaction performMoneyTransfer(Account sender, Account receiver, BigDecimal amount, Currency currency) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.update(sender);
        accountRepository.update(receiver);

        return transactionRepository.save(transaction);
    }
}
