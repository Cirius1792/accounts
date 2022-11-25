package com.clt.accounts.client;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.clt.accounts.client.dto.BalanceDto;
import com.clt.accounts.client.dto.TransactionDto;
import com.clt.common.error.ExternalServiceError;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@WireMockTest
public class AccountsClientTest {
  String apiKey = "XXXX";
  String authSchema = "S2S";

  @Test
  void testRetrieveBalance(WireMockRuntimeInfo wmRuntimeInfo) {
    String testAccount = "999";
    WireMock mockServer = wmRuntimeInfo.getWireMock();
    mockServer.register(WireMock.get(WireMock.urlMatching("/api/gbs/banking/v4.0/accounts/" + testAccount + "/balance"))
        .withHeader("Api-Key", WireMock.equalTo(apiKey))
        .withHeader("Auth-Schema", WireMock.equalTo(authSchema))
        .willReturn(WireMock.okJson("""
            {
              "status": "OK",
              "error": [],
              "payload":{
                  "date": "2018-08-17",
                  "balance": 29.64,
                  "availableBalance": 29.64,
                  "currency": "EUR"
                }
            }      """)));
    Long accountId = Long.valueOf(testAccount);
    BalanceDto expected = BalanceDto.builder()
        .availableBalance(BigDecimal.valueOf(29.64))
        .balance(BigDecimal.valueOf(29.64))
        .currency("EUR")
        .date("2018-08-17")
        .build();
    AccountsClient service = new AccountsClientImpl(wmRuntimeInfo.getHttpBaseUrl(), apiKey);
    Mono<BalanceDto> response = service.retrieveBalance(accountId);
    StepVerifier.create(response)
        .expectNext(expected)
        .verifyComplete();
  }

  @Test
  void testRetrieveTransactions(WireMockRuntimeInfo wmRuntimeInfo) {
    String fromDate = "2019-04-01";
    String toDate = "2019-04-01";
    String testAccount = "998";
    WireMock mockServer = wmRuntimeInfo.getWireMock();
    mockServer.register(
        WireMock.get(WireMock.urlPathMatching("/api/gbs/banking/v4.0/accounts/" + testAccount + "/transactions"))
            .withHeader("Api-Key", WireMock.equalTo(apiKey))
            .withHeader("Auth-Schema", WireMock.equalTo(authSchema))
            .withQueryParam("fromAccountingDate", WireMock.equalTo(fromDate))
            .withQueryParam("toAccountingDate", WireMock.equalTo(toDate))
            .willReturn(WireMock.okJson("""
                {
                  "status": "OK",
                  "error": [],
                  "payload": {
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
                  }
                        """)));
    AccountsClient service = new AccountsClientImpl(wmRuntimeInfo.getHttpBaseUrl(), apiKey);
    Flux<TransactionDto> transactions = service.retrieveTransactions(Long.valueOf(testAccount),
        LocalDate.parse(fromDate),
        LocalDate.parse(toDate));
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

  @Test
  public void testRetrieveBalanceWrongAccount(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wm = wmRuntimeInfo.getWireMock();
    String testAccount = "997";
    String errorCode = "REQ004";
    wm.register(WireMock.get("/api/gbs/banking/v4.0/accounts/" + testAccount + "/balance")
        .willReturn(WireMock.jsonResponse(String.format("""
              {
                "status": "KO",
                "errors": [
                  {
                    "code": "%s",
                    "description": "Invalid account identifier",
                    "params": ""
                  }
                ],
                "payload": {}
              }
            """, errorCode), 403)));
    AccountsClient service = new AccountsClientImpl(wmRuntimeInfo.getHttpBaseUrl(), apiKey);
    Mono<BalanceDto> response = service.retrieveBalance(Long.valueOf(testAccount));
    StepVerifier.create(response)
        .expectErrorMatches(throwable -> throwable instanceof ExternalServiceError &&
            errorCode.equals(throwable.getMessage()))
        .verify();
  }
}
