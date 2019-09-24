package com.laurynas.banktransfer.jpa.repository.impl;

import com.laurynas.banktransfer.jpa.model.Account;
import com.laurynas.banktransfer.jpa.repository.AccountRepository;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Singleton
public class AccountRepositoryImpl implements AccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Account.class, id));
    }

    @Override
    @Transactional
    public Account save(Account account) {
        entityManager.persist(account);
        return account;
    }

    @Override
    @Transactional
    public Account update(Account account) {
        entityManager.merge(account);
        return account;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        String qlString = "SELECT e FROM " + Account.class.getName() + " e";
        TypedQuery<Account> query = entityManager.createQuery(qlString, Account.class);
        return query.getResultList();
    }
}
