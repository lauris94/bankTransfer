package com.laurynas.banktransfer.api.service.mapper;

import com.laurynas.banktransfer.api.model.CreateUserRequest;
import com.laurynas.banktransfer.api.model.GetUserResponse;
import com.laurynas.banktransfer.api.model.ObjectRef;
import com.laurynas.banktransfer.jpa.model.User;

import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class UserMapper {

    public User toUserEntity(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setPersonalCode(createUserRequest.getPersonalCode());
        return user;
    }

    public GetUserResponse toGetUserResponse(User entity) {
        GetUserResponse user = new GetUserResponse();
        user.setId(entity.getId());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setPersonalCode(entity.getPersonalCode());

        List<ObjectRef> accountRefs = entity.getAccounts().stream()
                .map(AccountMapper::toAccountRef)
                .collect(Collectors.toList());

        user.setAccounts(accountRefs);
        return user;
    }
}
