package com.laurynas.banktransfer.jpa.repository;

import com.laurynas.banktransfer.jpa.model.Transaction;

public interface TransactionRepository {

    Transaction save(Transaction transaction);
}
