package com.clt.accounts.client;

import java.time.LocalDate;

import com.clt.accounts.client.dtos.BalanceDto;
import com.clt.accounts.client.dtos.TransactionDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountsClient {
    Mono<BalanceDto> retrieveBalance(Long accountId);
    Flux<TransactionDto> retrieveTransactions(Long accountId, LocalDate fromAccountingDate, LocalDate toAccountingDate);
}
