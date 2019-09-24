package com.laurynas.banktransfer.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.laurynas.banktransfer.jpa.model.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GetTransactionResponse {

    private Long id;
    private ObjectRef sender;
    private ObjectRef receiver;
    private BigDecimal amount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private Currency currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ObjectRef getSender() {
        return sender;
    }

    public void setSender(ObjectRef sender) {
        this.sender = sender;
    }

    public ObjectRef getReceiver() {
        return receiver;
    }

    public void setReceiver(ObjectRef receiver) {
        this.receiver = receiver;
    }
}
