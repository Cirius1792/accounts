package com.clt.accounts.services;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.clt.accounts.dtos.BalanceDto;
import com.clt.accounts.dtos.TransactionDto;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@WireMockTest
public class AccountsClientTest {
    //TODO: Add checks for request headers
    @Test
    void testRetrieveBalance(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMock mockServer = wmRuntimeInfo.getWireMock();
        mockServer.register(WireMock.get(WireMock.urlMatching("/api/gbs/banking/v4.0/accounts/[0-9]+/balance"))
                .willReturn(WireMock.okJson("""
                        {
                            "date": "2018-08-17",
                            "balance": 29.64,
                            "availableBalance": 29.64,
                            "currency": "EUR"
                          }
                            """)));
        Long accountId = 999L;
        BalanceDto expected = BalanceDto.builder()
                .availableBalance(BigDecimal.valueOf(29.64))
                .balance(BigDecimal.valueOf(29.64))
                .currency("EUR")
                .date("2018-08-17")
                .build();
        IAccountsClient service = new AccountsClient(wmRuntimeInfo.getHttpBaseUrl(), WebClient.builder());
        Mono<BalanceDto> response = service.retrieveBalance(accountId);
        StepVerifier.create(response)
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testRetrieveTransactions(WireMockRuntimeInfo wmRuntimeInfo) {
        String fromDate = "2019-04-01";
        String toDate = "2019-04-01";
        WireMock mockServer = wmRuntimeInfo.getWireMock();
        mockServer.register(WireMock.get(WireMock.urlPathMatching("/api/gbs/banking/v4.0/accounts/[0-9]+/transactions"))
                .withQueryParam("fromAccountingDate", WireMock.equalTo(fromDate))
                .withQueryParam("toAccountingDate", WireMock.equalTo(toDate))
                .willReturn(WireMock.okJson("""
                        {
                            "list": [
                              {
                                "transactionId": "1331714087",
                                "operationId": "00000000273015",
                                "accountingDate": "2019-04-01",
                                "valueDate": "2019-04-01",
                                "type": {
                                  "enumeration": "GBS_TRANSACTION_TYPE",
                                  "value": "GBS_TRANSACTION_TYPE_0023"
                                },
                                "amount": -800,
                                "currency": "EUR",
                                "description": "BA JOHN DOE PAYMENT INVOICE 75/2017"
                              },
                              {
                                "transactionId": "1331714088",
                                "operationId": "00000000273015",
                                "accountingDate": "2019-04-01",
                                "valueDate": "2019-04-01",
                                "type": {
                                  "enumeration": "GBS_TRANSACTION_TYPE",
                                  "value": "GBS_TRANSACTION_TYPE_0015"
                                },
                                "amount": -1,
                                "currency": "EUR",
                                "description": "CO MONEY TRANSFER FEES"
                              }
                            ]
                          }
                                """)));
        IAccountsClient service = new AccountsClient(wmRuntimeInfo.getHttpBaseUrl(), WebClient.builder());
        Flux<TransactionDto> transactions = service.retrieveTransactions(999L, Date.valueOf(fromDate),
                Date.valueOf(toDate));
        StepVerifier.create(transactions)
                .expectNext(TransactionDto.builder()
                            .transactionId(1331714087L)
                            .operationId("00000000273015")
                            .accountingDate("2019-04-01")
                            .valueDate("2019-04-01")
                            .amount(BigDecimal.valueOf(-800L))
                            .currency("EUR")
                            .description("BA JOHN DOE PAYMENT INVOICE 75/2017")
                        .build(),
                        TransactionDto.builder()
                            .transactionId(1331714088L)
                            .operationId("00000000273015")
                            .accountingDate("2019-04-01")
                            .valueDate("2019-04-01")
                            .amount(BigDecimal.valueOf(-1L))
                            .currency("EUR")
                            .description("CO MONEY TRANSFER FEES")
                        .build())
                .verifyComplete();
    }
}
