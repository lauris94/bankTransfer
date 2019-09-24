package com.laurynas.banktransfer.api.controller;

import com.laurynas.banktransfer.api.model.CreateAccountRequest;
import com.laurynas.banktransfer.api.model.CreateTransactionRequest;
import com.laurynas.banktransfer.api.model.CreateUserRequest;
import com.laurynas.banktransfer.jpa.model.Account;
import com.laurynas.banktransfer.jpa.model.Currency;
import com.laurynas.banktransfer.jpa.model.Transaction;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionControllerTest {

    private static EmbeddedServer server;
    private static HttpClient client;

    @BeforeAll
    public static void setupServerAndCreateTestData() {
        server = ApplicationContext
                .build()
                .run(EmbeddedServer.class);
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL());

        createTwoUsers();
        createTwoAccounts();
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
    public void transactionApiTest_createTransactionWithNegativeAmount_returnedBadRequest400() {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setSenderAccountId(3L);
        createTransactionRequest.setReceiverAccountId(4L);
        createTransactionRequest.setCurrency(Currency.EUR);
        createTransactionRequest.setAmount(new BigDecimal(-1));

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange(createTransactionPostRequest(createTransactionRequest)));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void transactionApiTest_createTransactionWithNoInfo_returnedBadRequest400() {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange(createTransactionPostRequest(createTransactionRequest)));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void transactionApiTest_createTransactionWithNonExistingAccount_returnedNotFound404() {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setSenderAccountId(30L);
        createTransactionRequest.setReceiverAccountId(4L);
        createTransactionRequest.setCurrency(Currency.EUR);
        createTransactionRequest.setAmount(new BigDecimal(5));

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange(createTransactionPostRequest(createTransactionRequest)));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void transactionApiTest_createTransactionWithTooLargeAmount_returnedBadRequest400() {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setSenderAccountId(3L);
        createTransactionRequest.setReceiverAccountId(4L);
        createTransactionRequest.setCurrency(Currency.EUR);
        createTransactionRequest.setAmount(new BigDecimal(1000));

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange(createTransactionPostRequest(createTransactionRequest)));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void transactionApiTest_createTransaction_transactionCreatedAndMoneyTransfered() {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setSenderAccountId(3L);
        createTransactionRequest.setReceiverAccountId(4L);
        createTransactionRequest.setCurrency(Currency.EUR);
        createTransactionRequest.setAmount(new BigDecimal(5));

        HttpResponse<Transaction> response = client.toBlocking().exchange(createTransactionPostRequest(createTransactionRequest));
        assertEquals(HttpStatus.OK, response.getStatus());

        Account firstAccount = client.toBlocking().retrieve(getAccountByIdRequest(3), Account.class);
        assertEquals(0, BigDecimal.ZERO.compareTo(firstAccount.getBalance()));

        Account secondAccount = client.toBlocking().retrieve(getAccountByIdRequest(4), Account.class);
        assertEquals(0, new BigDecimal(25).compareTo(secondAccount.getBalance()));
    }

    private static void createTwoUsers() {
        CreateUserRequest createCommand = new CreateUserRequest();
        createCommand.setFirstName("Laurynas");
        createCommand.setLastName("Cinga");
        createCommand.setPersonalCode("3942624564");

        HttpResponse<Account> response = client.toBlocking().exchange(createUserRequest(createCommand));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        createCommand.setFirstName("Jonas");
        createCommand.setLastName("Jonaitis");
        createCommand.setPersonalCode("3465645454");

        HttpResponse<Account> response2 = client.toBlocking().exchange(createUserRequest(createCommand));
        assertEquals(HttpStatus.CREATED, response2.getStatus());
    }

    private static void createTwoAccounts() {
        CreateAccountRequest createCommand = new CreateAccountRequest();
        createCommand.setBalance(new BigDecimal(5));
        createCommand.setCurrency(Currency.EUR);
        createCommand.setUserId(1L);

        HttpResponse<Account> response = client.toBlocking().exchange(createAccountRequest(createCommand));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        createCommand.setBalance(new BigDecimal(20));
        createCommand.setUserId(2L);
        HttpResponse<Account> response2 = client.toBlocking().exchange(createAccountRequest(createCommand));
        assertEquals(HttpStatus.CREATED, response2.getStatus());
    }

    private HttpRequest<CreateTransactionRequest> createTransactionPostRequest(CreateTransactionRequest createTransactionRequest) {
        return HttpRequest.POST("/api/transactions", createTransactionRequest);
    }

    private HttpRequest<Account> getAccountByIdRequest(int id) {
        return HttpRequest.GET("/api/accounts/" + id);
    }

    private static HttpRequest<CreateUserRequest> createUserRequest(CreateUserRequest createUserRequest) {
        return HttpRequest.POST("/api/users", createUserRequest);
    }

    private static HttpRequest<CreateAccountRequest> createAccountRequest(CreateAccountRequest createAccountRequest) {
        return HttpRequest.POST("/api/accounts", createAccountRequest);
    }
}
