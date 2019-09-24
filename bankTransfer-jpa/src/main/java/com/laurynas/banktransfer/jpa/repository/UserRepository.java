package com.laurynas.banktransfer.jpa.repository;

import com.laurynas.banktransfer.jpa.model.User;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(@NotNull Long id);

    User save(User user);

    User update(User user);

    List<User> findAll();
}
