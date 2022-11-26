package com.clt.payments.client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

import com.clt.accounts.client.dto.BalanceDto;
import com.clt.common.error.ExternalServiceError;
import com.clt.payments.client.dto.AccountDto;
import com.clt.payments.client.dto.CreditorDto;
import org.junit.jupiter.api.Test;

import com.clt.payments.client.dto.PaymentRequestDto;
import com.clt.payments.client.dto.PaymentResponseDto;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@WireMockTest
public class PaymentClientImplTest {

    String apiKey = "XXXX";
    String authSchema = "S2S";
    String zoneId = ZoneId.of(ZoneId.SHORT_IDS.get("ECT")).getId();

    String paymentRequestBody = """
            {
              "creditor": {
                "name": "%s",
                "account": {
                  "accountCode": "%s"
                }
              },
              "executionDate": "%s",
              "description": "%s",
              "amount": %s,
              "currency": "%s"
            }
            """;

    String paymentOkBody = """
            {
              "status": "OK",
              "error": [],
              "payload":{
                "moneyTransferId": "452516859427",
                "status": "EXECUTED",
                "direction": "OUTGOING",
                "creditor": {
                  "name": "John Doe",
                  "account": {
                    "accountCode": "IT23A0336844430152923804660",
                    "bicCode": "SELBIT2BXXX"
                  },
                  "address": {
                    "address": null,
                    "city": null,
                    "countryCode": null
                  }
                },
                "debtor": {
                  "name": "",
                  "account": {
                    "accountCode": "IT61F0326802230280596327270",
                    "bicCode": null
                  }
                },
                "cro": "1234566788907",
                "uri": "REMITTANCE_INFORMATION",
                "trn": "AJFSAD1234566788907CCSFDGTGVGV",
                "description": "Description",
                "createdDatetime": "2019-04-10T10:38:55.949+0200",
                "accountedDatetime": "2019-04-10T10:38:56.000+0200",
                "debtorValueDate": "2019-04-10",
                "creditorValueDate": "2019-04-10",
                "amount": {
                  "debtorAmount": 800,
                  "debtorCurrency": "EUR",
                  "creditorAmount": 800,
                  "creditorCurrency": "EUR",
                  "creditorCurrencyDate": "2019-04-10",
                  "exchangeRate": 1
                },
                "isUrgent": false,
                "isInstant": false,
                "feeType": "SHA",
                "feeAccountId": "12345678",
                "fees": [
                  {
                    "feeCode": "MK001",
                    "description": "Money transfer execution fee",
                    "amount": 0.25,
                    "currency": "EUR"
                  },
                  {
                    "feeCode": "MK003",
                    "description": "Currency exchange fee",
                    "amount": 3.5,
                    "currency": "EUR"
                  }
                ],
                "hasTaxRelief": true
              }
            }      
            """;

    @Test
    void testPostPayment(WireMockRuntimeInfo wmRuntimeInfo) {
        String accountId = "999";
        PaymentRequestDto request = prepareRequestDto();

        String requestBody = this.prepareRequest(request.getCreditor().getName(),
                request.getCreditor().getAccount().getAccountCode(),
                request.getExecutionDate().toString(),
                request.getDescription(),
                request.getAmount(),
                request.getCurrency());

        WireMock mockServer = wmRuntimeInfo.getWireMock();
        mockServer.register(
                WireMock.post(WireMock
                                .urlMatching("/api/gbs/banking/v4.0/accounts/" + accountId + "/payments/money-transfers"))
                        .withHeader("Api-Key", WireMock.equalTo(apiKey))
                        .withHeader("Auth-Schema", WireMock.equalTo(authSchema))
                        .withHeader("X-Time-Zone", WireMock.equalTo(zoneId))
                        .withRequestBody(WireMock.equalToJson(requestBody))
                        .willReturn(WireMock.okJson(paymentOkBody)));

        PaymentResponseDto expected = PaymentResponseDto.builder()
                .moneyTransferId("452516859427")
                .status("EXECUTED")
                .direction("OUTGOING")
                .build();
        PaymentClient client = new PaymentClientImpl(wmRuntimeInfo.getHttpBaseUrl(), apiKey, zoneId);

        Mono<PaymentResponseDto> actual = client.postPayment(Long.valueOf(accountId), request);
        StepVerifier.create(actual)
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testPostPaymentLimitedAccount(WireMockRuntimeInfo wmRuntimeInfo) {
        PaymentRequestDto request = this.prepareRequestDto();
        String errorCode = "APIXXX";
        String paymentKoBody = """
                {
                    "status": "OK",
                    "errorDtos": [
                        {
                            "code": "%s",
                            "description": "Errore tecnico",
                            "params": ""
                        }
                    ],
                "payload": {}
                }
                """;

        WireMock mockServer = wmRuntimeInfo.getWireMock();
        String accountId = "7";
        mockServer.register(
                WireMock.post(WireMock
                                .urlMatching("/api/gbs/banking/v4.0/accounts/" + accountId + "/payments/money-transfers"))
                        .willReturn(WireMock.jsonResponse(String.format(paymentKoBody, errorCode), 400)));
        PaymentClient client = new PaymentClientImpl(wmRuntimeInfo.getHttpBaseUrl(), apiKey, zoneId);
        Mono<PaymentResponseDto> actual = client.postPayment(Long.valueOf(accountId), request);

        StepVerifier.create(actual)
                .expectErrorMatches(throwable -> throwable instanceof ExternalServiceError &&
                        errorCode.equals(throwable.getMessage()))
                .verify();
    }

    protected PaymentRequestDto prepareRequestDto() {
        return PaymentRequestDto.builder()
                .creditor(CreditorDto.builder()
                        .name("John Doe")
                        .account(AccountDto.builder().accountCode("FR7630006000011234567890189").build())
                        .build())
                .amount(BigDecimal.valueOf(100.0))
                .currency("EUR")
                .description("Gift")
                .executionDate(LocalDate.now())
                .build();
    }

    protected String prepareRequest(String creditorName,
                                    String creditorAccountDetails,
                                    String executionDate,
                                    String description,
                                    BigDecimal amount,
                                    String currency) {
        return String.format(paymentRequestBody,
                creditorName,
                creditorAccountDetails,
                executionDate,
                description,
                amount,
                currency);
    }
}
