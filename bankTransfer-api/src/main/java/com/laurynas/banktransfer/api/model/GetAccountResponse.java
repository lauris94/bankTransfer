package com.laurynas.banktransfer.api.model;

import com.laurynas.banktransfer.jpa.model.Currency;

import java.math.BigDecimal;

public class GetAccountResponse {

    private Long id;
    private String iban;
    private BigDecimal balance;
    private Currency currency;
    private ObjectRef user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public ObjectRef getUser() {
        return user;
    }

    public void setUser(ObjectRef user) {
        this.user = user;
    }
}
