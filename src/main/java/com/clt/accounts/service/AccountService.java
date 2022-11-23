package com.clt.accounts.service;

import java.time.LocalDate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

    Mono<BalanceEntity> retrieveBalance();
    Flux<TransactionEntity> retrieveTransactions(LocalDate fromDate, LocalDate toDate);
}
