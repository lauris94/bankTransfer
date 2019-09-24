package com.laurynas.banktransfer.api.model;

import com.laurynas.banktransfer.jpa.model.Currency;
import io.micronaut.validation.Validated;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Validated
public class CreateAccountRequest {

    @NotNull
    private BigDecimal balance;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Currency currency;

    private Long userId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
