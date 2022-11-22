package com.clt.accounts.handlers;

import com.clt.accounts.handlers.responses.BalanceResponse;
import com.clt.accounts.service.AccountService;

import reactor.core.publisher.Mono;

public class AccountsHandler {

    final AccountService accountService;

    public AccountsHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    public Mono<BalanceResponse> getBalance() {
        return accountService.retrieveBalance().map(balance -> BalanceResponse.builder()
                .date(balance.getDate())
                .availableBalance(balance.getAvailableBalance())
                .balance(balance.getBalance())
                .currency(balance.getCurrency())
                .build());
    }
}
