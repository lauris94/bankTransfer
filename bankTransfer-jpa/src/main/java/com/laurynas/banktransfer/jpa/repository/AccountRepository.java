package com.laurynas.banktransfer.jpa.repository;

import com.laurynas.banktransfer.jpa.model.Account;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById(@NotNull Long id);

    Account save(Account account);

    Account update(Account account);

    List<Account> findAll();
}
