package com.clt.accounts.routers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.clt.accounts.component.AccountComponent;
import com.clt.accounts.component.BalanceEntity;
import com.clt.accounts.component.TransactionEntity;
import com.clt.accounts.router.AccountRouter;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AccountRouterTest {
    AccountComponent accountService;
    private WebTestClient client;
    private LocalDate testTime;

    @BeforeEach
    void setUp() {
        testTime = LocalDate.of(2022, 11, 23);
        Clock clock = Clock.fixed(testTime.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        accountService = mock(AccountComponent.class);
        RouterFunction<?> routes = new AccountRouter(accountService, clock).accountApis();
        client = WebTestClient.bindToRouterFunction(routes)
                .build();
    }

    @Test
    void testGetBalanceReturnsOK(){
        when(accountService.retrieveBalance())
        .thenReturn(Mono.just(BalanceEntity.builder()
                            .date(testTime)
                            .availableBalance(BigDecimal.valueOf(10.0))
                            .balance(BigDecimal.valueOf(10.0))
                            .build()));
        client.get().uri("/balance")
        .exchange()
        .expectStatus().isOk();
    }

    @Test
    void testGetTransactionsReturnsOk() {
        String dateTo = "2022-01-01";
        String dateFrom = "2022-01-02";
        TransactionEntity testTransaction = TransactionEntity.builder()
                .transactionId("000000001")
                .operationId("9999999999")
                .valueDate(LocalDate.parse("2019-11-09"))
                .amount(BigDecimal.valueOf(1000000.0))
                .currency("EUR")
                .description("Desc")
                .build();
        when(accountService.retrieveTransactions(Mockito.eq(LocalDate.parse(dateFrom)),
                Mockito.eq(LocalDate.parse(dateTo))))
                .thenReturn(Flux.just(testTransaction));

        client.get().uri(uribuilder -> uribuilder.path("/transactions")
                .queryParam("dateTo", dateTo)
                .queryParam("dateFrom", dateFrom)
                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.transactions[0].transactionId").isEqualTo(testTransaction.getTransactionId())
                .jsonPath("$.transactions[0].operationId").isEqualTo(testTransaction.getOperationId())
                .jsonPath("$.transactions[0].valueDate").isEqualTo(testTransaction.getValueDate().toString())
                .jsonPath("$.transactions[0].currency").isEqualTo(testTransaction.getCurrency())
                .jsonPath("$.transactions[0].description").isEqualTo(testTransaction.getDescription())
                .jsonPath("$.transactions[0].amount").isEqualTo(testTransaction.getAmount())
        ;
    }

    @Test
    void testGetTransactionsReturnsOkNoInputDates() {
        when(accountService.retrieveTransactions(Mockito.eq(testTime),
                Mockito.eq(testTime)))
                .thenReturn(Flux.just(TransactionEntity.builder()
                .accountingDate(testTime)
                .amount(BigDecimal.valueOf(100.0))
                .build()));

        client.get().uri(uribuilder -> uribuilder.path("/transactions")
                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.transactions[0].accountingDate")
                .isEqualTo("2022-11-23");
    }

}
