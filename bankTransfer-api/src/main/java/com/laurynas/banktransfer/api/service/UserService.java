package com.laurynas.banktransfer.api.service;

import com.laurynas.banktransfer.api.model.CreateUserRequest;
import com.laurynas.banktransfer.jpa.model.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    List<User> getAllUsers();

    User createUser(CreateUserRequest createUserRequest);
}
