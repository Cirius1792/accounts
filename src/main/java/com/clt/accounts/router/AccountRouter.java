package com.clt.accounts.router;

import java.time.Clock;
import java.time.LocalDate;
import java.util.stream.Collectors;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.clt.accounts.component.AccountComponent;
import com.clt.accounts.router.response.BalanceResponseDto;
import com.clt.accounts.router.response.TransactionResponseDto;
import com.clt.accounts.router.response.TransactionsResponseDto;

import reactor.core.publisher.Mono;

public class AccountRouter {
    final AccountComponent accountService;
    final Clock clock;

    public AccountRouter(AccountComponent accountService, Clock clock) {
        this.accountService = accountService;
        this.clock = clock;
    }

    Mono<ServerResponse> getBalance(ServerRequest request) {
        return ServerResponse.ok()
                .body(accountService.retrieveBalance()
                                .map(BalanceResponseDto::toDto),
                        BalanceResponseDto.class);
    }

    Mono<ServerResponse> getTransactions(ServerRequest request) {
        LocalDate to = request.queryParam("dateTo").map(LocalDate::parse).orElse(LocalDate.now(clock));
        LocalDate from = request.queryParam("dateFrom").map(LocalDate::parse).orElse(to);
        return ServerResponse.ok()
                .body(accountService.retrieveTransactions(from, to)
                                .log()
                                .map(TransactionResponseDto::toDto)
                                .collect(Collectors.toList())
                                .map(transactions -> TransactionsResponseDto.builder()
                                        .transactions(transactions).build()),
                        TransactionsResponseDto.class);
    }

    public RouterFunction<ServerResponse> accountApis() {
        return RouterFunctions.route()
                .path("/balance", builder -> builder
                        .GET("", this::getBalance))
                .GET("/transactions", this::getTransactions)
                .build();
    }
}
