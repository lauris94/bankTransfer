package com.laurynas.banktransfer.api.service.impl;

import com.laurynas.banktransfer.api.exception.NotFoundException;
import com.laurynas.banktransfer.api.model.CreateUserRequest;
import com.laurynas.banktransfer.api.service.UserService;
import com.laurynas.banktransfer.api.service.mapper.UserMapper;
import com.laurynas.banktransfer.jpa.model.User;
import com.laurynas.banktransfer.jpa.repository.UserRepository;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist", id)));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        User userToSave = userMapper.toUserEntity(createUserRequest);
        return userRepository.save(userToSave);
    }
}
