package com.laurynas.banktransfer.api.service.mapper;

import com.laurynas.banktransfer.api.model.GetTransactionResponse;
import com.laurynas.banktransfer.jpa.model.Transaction;

import javax.inject.Singleton;

import static com.laurynas.banktransfer.api.service.mapper.AccountMapper.toAccountRef;

@Singleton
public class TransactionMapper {

    public GetTransactionResponse toGetTransactionResponse(Transaction entity) {
        GetTransactionResponse getTransactionResponse = new GetTransactionResponse();
        getTransactionResponse.setId(entity.getId());
        getTransactionResponse.setCurrency(entity.getCurrency());
        getTransactionResponse.setTimestamp(entity.getTimestamp());
        getTransactionResponse.setAmount(entity.getAmount());

        if (entity.getSender() != null) {
            getTransactionResponse.setSender(toAccountRef(entity.getSender()));
        }
        if (entity.getReceiver() != null) {
            getTransactionResponse.setReceiver(toAccountRef(entity.getReceiver()));
        }

        return getTransactionResponse;
    }


}
