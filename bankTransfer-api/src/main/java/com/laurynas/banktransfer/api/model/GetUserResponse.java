package com.laurynas.banktransfer.api.model;

import java.util.List;

public class GetUserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String personalCode;
    private List<ObjectRef> accounts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public List<ObjectRef> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<ObjectRef> accounts) {
        this.accounts = accounts;
    }
}
