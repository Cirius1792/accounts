package com.clt.accounts.router;

import java.time.Clock;
import java.time.LocalDate;
import java.util.stream.Collectors;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.clt.accounts.component.AccountComponent;
import com.clt.accounts.router.responses.BalanceResponse;
import com.clt.accounts.router.responses.TransactionResponse;
import com.clt.accounts.router.responses.TransactionsResponse;

import reactor.core.publisher.Mono;

public class AccountRouterImpl {
        final AccountComponent accountService;
        final Clock clock;

        public AccountRouterImpl(AccountComponent accountService, Clock clock) {
                this.accountService = accountService;
                this.clock = clock;
        }

        Mono<ServerResponse> getBalance(ServerRequest request) {
                return ServerResponse.ok()
                                .body(accountService.retrieveBalance()
                                                .log()
                                                .map(balance -> BalanceResponse.builder()
                                                                .date(balance.getDate())
                                                                .availableBalance(balance.getAvailableBalance())
                                                                .balance(balance.getBalance())
                                                                .currency(balance.getCurrency())
                                                                .build()),
                                                BalanceResponse.class);
        }

        Mono<ServerResponse> getTransactions(ServerRequest request) {
                LocalDate to = request.queryParam("dateTo").map(LocalDate::parse).orElse(LocalDate.now(clock));
                LocalDate from = request.queryParam("dateFrom").map(LocalDate::parse).orElse(to);
                return ServerResponse.ok()
                                .body(accountService.retrieveTransactions(from, to)
                                                .map(transaction -> TransactionResponse.builder()
                                                                .accountingDate(transaction.getAccountingDate())
                                                                .valueDate(transaction.getValueDate())
                                                                .amount(transaction.getAmount())
                                                                .currency(transaction.getCurrency())
                                                                .description(transaction.getDescription())
                                                                .build())
                                                .collect(Collectors.toList())
                                                .map(transactions -> TransactionsResponse.builder()
                                                                .transactions(transactions).build()),
                                                TransactionsResponse.class);
        }

        public RouterFunction<ServerResponse> accountApis() {
                return RouterFunctions.route()
                                .path("/balance", builder -> builder
                                                .GET("", this::getBalance))
                                .GET("/transactions", this::getTransactions)
                                .build();
        }
}
