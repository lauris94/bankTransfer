package com.laurynas.banktransfer.api.service;

import com.laurynas.banktransfer.jpa.model.Account;
import com.laurynas.banktransfer.jpa.model.Currency;
import com.laurynas.banktransfer.jpa.model.Transaction;

import java.math.BigDecimal;

public interface TransactionService {

    Transaction performMoneyTransfer(Account sender, Account receiver, BigDecimal amount, Currency currency);
}
