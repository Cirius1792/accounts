package com.clt.accounts.routers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.clt.accounts.service.AccountService;
import com.clt.accounts.service.BalanceEntity;
import com.clt.accounts.service.TransactionEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AccountRouterImplTest {
    AccountService accountService;
    private WebTestClient client;
    private LocalDate testTime;
    private static String DATE_TIME_FORMAT = "yyyy-MM-dd";
    private String testTimeString;

    @BeforeEach
    void setUp() {
        testTime = LocalDate.of(2022, 11, 23);
        testTimeString = testTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        Clock clock = Clock.fixed(testTime.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        accountService = mock(AccountService.class);
        RouterFunction<?> routes = new AccountRouterImpl(accountService, clock).accountApis();
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
        when(accountService.retrieveTransactions(Mockito.eq(LocalDate.parse(dateFrom)),
                Mockito.eq(LocalDate.parse(dateTo))))
                .thenReturn(Flux.empty());

        client.get().uri(uribuilder -> uribuilder.path("/transactions")
                .queryParam("dateTo", dateTo)
                .queryParam("dateFrom", dateFrom)
                .build())
                .exchange()
                .expectStatus().isOk();
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
