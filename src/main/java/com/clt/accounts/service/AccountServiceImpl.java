package com.clt.accounts.service;

import java.util.Date;

import com.clt.accounts.client.AccountsClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AccountServiceImpl implements AccountService {

    final AccountsClient accountsClient;
    final Long accountNumber;

    public AccountServiceImpl(Long accountNumber, AccountsClient accountsClient) {
        if(accountNumber == null)
            throw new IllegalArgumentException("Account number can't be null");
        this.accountNumber = accountNumber;
        this.accountsClient = accountsClient;
    }

    @Override
    public Mono<BalanceEntity> retrieveBalance() {
        return this.accountsClient.retrieveBalance(this.accountNumber)
                .map(balance -> BalanceEntity.builder()
                        .availableBalance(balance.getAvailableBalance())
                        .balance(balance.getBalance())
                        .currency(balance.getCurrency())
                        .date(balance.getDate())
                        .build());
    }

    @Override
    public Flux<TransactionEntity> retrieveTransactions(Date fromDate, Date toDate) {
        return this.accountsClient.retrieveTransactions(this.accountNumber, fromDate, toDate)
                .map(transaction -> TransactionEntity.builder()
                        .accountingDate(transaction.getAccountingDate())
                        .amount(transaction.getAmount())
                        .currency(transaction.getCurrency())
                        .description(transaction.getDescription())
                        .build());
    }

}
