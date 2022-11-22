package com.clt.accounts.service;

import java.util.Date;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

    Mono<BalanceEntity> retrieveBalance();
    Flux<TransactionEntity> retrieveTransactions(Date fromDate, Date toDate);
}
