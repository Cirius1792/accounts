package com.clt.accounts.client;

import java.time.LocalDate;

import org.springframework.web.reactive.function.client.WebClient;

import com.clt.accounts.client.dtos.BalanceDto;
import com.clt.accounts.client.dtos.TransactionDto;
import com.clt.accounts.client.dtos.TransactionsDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AccountsClientImpl implements AccountsClient {

    final String BALANCE_ENDPOINT = "/api/gbs/banking/v4.0/accounts/{accountId}/balance";
    final String TRANSACTION_ENDPOINT = "/api/gbs/banking/v4.0/accounts/{accountId}/transactions";

    static final String AUTH_SCHEMA = "S2S";

    final String basePath;
    final WebClient client;

    public AccountsClientImpl(String basePath, String apiKey) {
        this.basePath = basePath;
        this.client = WebClient.builder()
                .defaultHeader("Api-Key", apiKey)
                .defaultHeader("Auth-Schema", AUTH_SCHEMA)
                .baseUrl(this.basePath).build();
    }

    @Override
    public Mono<BalanceDto> retrieveBalance(Long accountId) {
        return this.client.get()
                .uri(BALANCE_ENDPOINT, accountId)
                .retrieve()
                .bodyToMono(BalanceDto.class);
    }

    @Override
    public Flux<TransactionDto> retrieveTransactions(Long accountId, LocalDate fromAccountingDate,
            LocalDate toAccountingDate) {
        return this.client.get()
                .uri(uriBuilder -> uriBuilder.path(TRANSACTION_ENDPOINT)
                        .queryParam("fromAccountingDate", fromAccountingDate.toString())
                        .queryParam("toAccountingDate", toAccountingDate.toString())
                        .build(accountId))
                .retrieve()
                .bodyToMono(TransactionsDto.class)
                .flatMapMany(el -> Flux.fromIterable(el.getList()));
    }

}
