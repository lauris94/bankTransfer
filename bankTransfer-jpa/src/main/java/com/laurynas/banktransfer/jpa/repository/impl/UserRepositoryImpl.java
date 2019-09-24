package com.laurynas.banktransfer.jpa.repository.impl;

import com.laurynas.banktransfer.jpa.model.User;
import com.laurynas.banktransfer.jpa.repository.UserRepository;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    @Transactional
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    @Transactional
    public User update(User user) {
        entityManager.merge(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        String qlString = "SELECT e FROM " + User.class.getName() + " e";
        TypedQuery<User> query = entityManager.createQuery(qlString, User.class);
        return query.getResultList();
    }
}
