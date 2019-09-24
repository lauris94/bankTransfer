package com.laurynas.banktransfer.api.model;

import com.laurynas.banktransfer.jpa.model.Currency;
import io.micronaut.validation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Validated
public class CreateTransactionRequest {

    @NotNull
    private Long senderAccountId;

    @NotNull
    private Long receiverAccountId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Currency currency;

    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(Long senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public Long getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(Long receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
