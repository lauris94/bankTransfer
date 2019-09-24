package com.laurynas.banktransfer.api.controller;

import com.laurynas.banktransfer.api.model.CreateUserRequest;
import com.laurynas.banktransfer.jpa.model.Account;
import com.laurynas.banktransfer.jpa.model.User;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

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
    public void createUser_missingPersonalCode_returnsClientError400() {
        CreateUserRequest createCommand = new CreateUserRequest();
        createCommand.setFirstName("Laurynas");
        createCommand.setLastName("Cinga");

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange(createUserRequest(createCommand)));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void getUserById_userDoesNotExist_returnsClientError404() {
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(getUserByIdRequest(27)));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void userApiTest_create_getById_getAll() {
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

        User firstUser = client.toBlocking().retrieve(getUserByIdRequest(1), User.class);
        assertEquals("Laurynas", firstUser.getFirstName());
        assertEquals("Cinga", firstUser.getLastName());
        assertEquals("3942624564", firstUser.getPersonalCode());

        User secondUser = client.toBlocking().retrieve(getUserByIdRequest(2), User.class);
        assertEquals("Jonas", secondUser.getFirstName());
        assertEquals("Jonaitis", secondUser.getLastName());
        assertEquals("3465645454", secondUser.getPersonalCode());

        List<User> allUsers = client.toBlocking().retrieve(getAllUsersRequest(), Argument.of(List.class, User.class));
        assertEquals(2, allUsers.size());
    }

    private HttpRequest<CreateUserRequest> createUserRequest(CreateUserRequest createCommand) {
        return HttpRequest.POST("/api/users", createCommand);
    }

    private HttpRequest<User> getUserByIdRequest(int id) {
        return HttpRequest.GET("/api/users/" + id);
    }

    private HttpRequest<Account> getAllUsersRequest() {
        return HttpRequest.GET("/api/users");
    }
}
