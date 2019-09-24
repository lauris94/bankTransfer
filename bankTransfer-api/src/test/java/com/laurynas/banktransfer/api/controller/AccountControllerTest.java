package com.laurynas.banktransfer.api.controller;

import com.laurynas.banktransfer.api.model.CreateAccountRequest;
import com.laurynas.banktransfer.jpa.model.Account;
import com.laurynas.banktransfer.jpa.model.Currency;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.reactivex.Single;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountControllerTest {

    private static EmbeddedServer server;
    private static HttpClient client;

    @BeforeAll
    public static void setupServer() {
        server = ApplicationContext
                .build()
                .run(EmbeddedServer.class);
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL());
    }

    @AfterAll
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    @Test
    public void createAccount_nonExistingPerson_returnsClientError404() {
        CreateAccountRequest createCommand = new CreateAccountRequest();
        createCommand.setBalance(new BigDecimal(5));
        createCommand.setCurrency(Currency.EUR);
        createCommand.setUserId(30L);

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange(createAccountRequest(createCommand)));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void createAccount_missingDetails_returnsClientError400() {
        CreateAccountRequest createCommand = new CreateAccountRequest();
        createCommand.setCurrency(Currency.EUR);

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange(createAccountRequest(createCommand)));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void getAccountById_accountDoesNotExist_returnsClientError404() {
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(getAccountByIdRequest(35L)));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void accountApiTest_create_getById_getAll() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBalance(new BigDecimal(5));
        createAccountRequest.setCurrency(Currency.EUR);

        HttpResponse<Account> response = callCreateAccount(createAccountRequest);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        Account createdFirstAccount = response.getBody(Account.class).get();

        createAccountRequest.setBalance(new BigDecimal(20));
        HttpResponse<Account> response2 = callCreateAccount(createAccountRequest);
        assertEquals(HttpStatus.CREATED, response2.getStatus());
        Account createdSecondAccount = response2.getBody(Account.class).get();

        Long firstAccountId = createdFirstAccount.getId();
        Long secondAccountId = createdSecondAccount.getId();

        Account firstAccount = client.toBlocking().retrieve(getAccountByIdRequest(firstAccountId), Account.class);
        assertEquals(Currency.EUR, firstAccount.getCurrency());
        assertEquals(5, firstAccount.getBalance().intValue());
        assertNotNull(firstAccount.getIban());

        Account secondAccount = client.toBlocking().retrieve(getAccountByIdRequest(secondAccountId), Account.class);
        assertEquals(Currency.EUR, secondAccount.getCurrency());
        assertEquals(20, secondAccount.getBalance().intValue());
        assertNotNull(secondAccount.getIban());

        List<Account> allAccounts = client.toBlocking().retrieve(getAllAccountsRequest(), Argument.of(List.class, Account.class));
        assertEquals(2, allAccounts.size());
    }

    private HttpResponse<Account> callCreateAccount(CreateAccountRequest createAccountRequest) {
        Single<HttpResponse<Account>> accSingle = Single.fromPublisher(client.exchange(
                createAccountRequest(createAccountRequest), Account.class
        ));
        return accSingle.blockingGet();
    }

    private HttpRequest<CreateAccountRequest> createAccountRequest(CreateAccountRequest createCommand) {
        return HttpRequest.POST("/api/accounts", createCommand);
    }

    private HttpRequest<Account> getAccountByIdRequest(Long id) {
        return HttpRequest.GET("/api/accounts/" + id);
    }

    private HttpRequest<Account> getAllAccountsRequest() {
        return HttpRequest.GET("/api/accounts");
    }
}
