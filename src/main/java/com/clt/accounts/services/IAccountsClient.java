package com.clt.accounts.services;

import java.util.Date;

import com.clt.accounts.dtos.BalanceDto;
import com.clt.accounts.dtos.TransactionDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountsClient {
    Mono<BalanceDto> retrieveBalance(Long accountId);
    Flux<TransactionDto> retrieveTransactions(Long accountId, Date fromAccountingDate, Date toAccountingDate);
}
