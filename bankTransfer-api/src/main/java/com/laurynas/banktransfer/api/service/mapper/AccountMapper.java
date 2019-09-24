package com.laurynas.banktransfer.api.service.mapper;

import com.laurynas.banktransfer.api.model.CreateAccountRequest;
import com.laurynas.banktransfer.api.model.GetAccountResponse;
import com.laurynas.banktransfer.api.model.ObjectRef;
import com.laurynas.banktransfer.api.service.IbanGenerator;
import com.laurynas.banktransfer.jpa.model.Account;
import com.laurynas.banktransfer.jpa.model.User;
import io.micronaut.http.hateoas.Link;

import javax.inject.Singleton;

@Singleton
public class AccountMapper {

    private static final String COUNTRY_CODE = "LT";

    private final IbanGenerator ibanGenerator;

    public AccountMapper(IbanGenerator ibanGenerator) {
        this.ibanGenerator = ibanGenerator;
    }

    public Account toAccountEntity(User owner, CreateAccountRequest createAccountRequest) {
        Account entity = toAccountEntity(createAccountRequest);
        entity.setUser(owner);
        return entity;
    }

    public Account toAccountEntity(CreateAccountRequest createAccountRequest) {
        Account entity = new Account();
        entity.setBalance(createAccountRequest.getBalance());
        entity.setCurrency(createAccountRequest.getCurrency());
        entity.setIban(ibanGenerator.generateIban(COUNTRY_CODE));
        return entity;
    }

    public GetAccountResponse toGetAccountResponse(Account entity) {
        GetAccountResponse getAccountResponse = new GetAccountResponse();
        getAccountResponse.setId(entity.getId());
        getAccountResponse.setBalance(entity.getBalance());
        getAccountResponse.setCurrency(entity.getCurrency());
        getAccountResponse.setIban(entity.getIban());

        if (entity.getUser() != null) {
            ObjectRef userRef = new ObjectRef();
            userRef.setId(entity.getUser().getId());
            userRef.setLink(Link.of("api/users/" + entity.getUser().getId()));
            getAccountResponse.setUser(userRef);
        }
        return getAccountResponse;
    }

    public static ObjectRef toAccountRef(Account account) {
        ObjectRef accountRef = new ObjectRef();
        accountRef.setId(account.getId());
        accountRef.setLink(Link.of("api/accounts/" + account.getId()));
        return accountRef;
    }
}
