package com.laurynas.banktransfer.api.controller;

import com.laurynas.banktransfer.api.model.CreateUserRequest;
import com.laurynas.banktransfer.api.model.GetUserResponse;
import com.laurynas.banktransfer.api.service.UserService;
import com.laurynas.banktransfer.api.service.mapper.UserMapper;
import com.laurynas.banktransfer.jpa.model.User;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Get
    public List<GetUserResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(userMapper::toGetUserResponse)
                .collect(Collectors.toList());
    }

    @Get(uri = "/{id}")
    public GetUserResponse getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return userMapper.toGetUserResponse(user);
    }

    @Post
    public HttpResponse<GetUserResponse> createUser(@Body @Valid CreateUserRequest personToCreate) {
        User createdUser = userService.createUser(personToCreate);
        return HttpResponse
                .created(userMapper.toGetUserResponse(createdUser));
    }
}
