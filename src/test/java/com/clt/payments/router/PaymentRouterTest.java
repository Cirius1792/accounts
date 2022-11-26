package com.clt.payments.router;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.clt.payments.component.PaymentComponent;
import com.clt.payments.component.PaymentEntity;
import com.clt.payments.component.PaymentReceiptEntity;
import com.clt.payments.router.request.PaymentRequestDto;

import reactor.core.publisher.Mono;

public class PaymentRouterTest {

    PaymentComponent paymentComponent;
    WebTestClient client;

    @BeforeEach
    void setUp() {
        paymentComponent = mock(PaymentComponent.class);
        client = WebTestClient.bindToRouterFunction(new PaymentRouter(paymentComponent).paymentApis())
                .build();
    }

    @Test
    void testPostPaymentOk() {
        double amount = 429.00;
        String receiverName = "Luisa";
        String receiverAccount = "ES7921000813610123456789";
        String currency = "EUR";
        String executionDate = "2022-12-01";
        PaymentEntity componentParameters = PaymentEntity.builder()
                .amount(BigDecimal.valueOf(amount))
                .receiverAccount(receiverAccount)
                .receiverName(receiverName)
                .currency(currency)
                .executionDate(LocalDate.parse(executionDate))
                .build();
        when(paymentComponent.executePayment(componentParameters))
                .thenReturn(Mono.just(PaymentReceiptEntity.builder()
                        .direction("OUTGOING")
                        .status("BOOKED")
                        .moneyTransferId("XXXXX-01")
                        .build()));
        client.post().uri("/payment")
                .body(Mono.just(PaymentRequestDto.builder()
                        .amount(BigDecimal.valueOf(amount))
                        .receiverAccount(receiverAccount)
                        .receiverName(receiverName)
                        .currency(currency)
                        .executionDate(LocalDate.parse(executionDate))
                        .build()), PaymentRequestDto.class)
                .exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.status").isEqualTo("BOOKED");
    }
}
