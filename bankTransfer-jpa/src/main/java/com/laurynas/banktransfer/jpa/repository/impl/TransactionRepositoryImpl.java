package com.laurynas.banktransfer.jpa.repository.impl;

import com.laurynas.banktransfer.jpa.model.Transaction;
import com.laurynas.banktransfer.jpa.repository.TransactionRepository;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class TransactionRepositoryImpl implements TransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Transaction save(Transaction transaction) {
        entityManager.persist(transaction);
        return transaction;
    }
}
