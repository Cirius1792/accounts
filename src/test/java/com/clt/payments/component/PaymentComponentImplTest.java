package com.clt.payments.component;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.clt.payments.client.PaymentClient;
import com.clt.payments.client.dto.PaymentResponseDto;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class PaymentComponentImplTest {

    PaymentComponent component;

    @BeforeEach
    public void initTest() {
        PaymentClient client = mock(PaymentClient.class);
        when(client.postPayment(eq("666"), any()))
                .thenReturn(Mono.just(PaymentResponseDto.builder()
                        .direction("OUTGOING")
                        .moneyTransferId("XXXX-1")
                        .status("BOOKED")
                        .build()));
        component = new PaymentComponentImpl("666", client);
    }

    @Test
    void testExecutePaymentOK() {
        PaymentIn componentParameters = PaymentIn.builder()
                .receiverName("Mario Rossi")
                .receiverAccount("FR7630006000011234567890189")
                .description("Take My Money")
                .amount(BigDecimal.valueOf(429.00))
                .currency("EUR")
                .build();

        Mono<PaymentOut> actual = component.executePayment(componentParameters);

        StepVerifier.create(actual)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testExecutePaymentMissingCurrency() {
        PaymentIn componentParameters = PaymentIn.builder()
                .receiverName("Mario Rossi")
                .receiverAccount("FR7630006000011234567890189")
                .description("Take My Money")
                .amount(BigDecimal.valueOf(429.00))
                .build();

        StepVerifier.create(component.executePayment(componentParameters))
                .verifyError();
    }

    @Test
    void testExecutePaymentMissingAmount() {
        PaymentIn componentParameters = PaymentIn.builder()
                .receiverName("Mario Rossi")
                .receiverAccount("FR7630006000011234567890189")
                .description("Take My Money")
                .currency("EUR")
                .build();

        StepVerifier.create(component.executePayment(componentParameters))
                .verifyError();
    }

    @Test
    void testExecutePaymentMissingAccount() {
        PaymentIn componentParameters = PaymentIn.builder()
                .receiverName("Mario Rossi")
                .description("Take My Money")
                .amount(BigDecimal.valueOf(429.00))
                .currency("EUR")
                .build();

        StepVerifier.create(component.executePayment(componentParameters))
                .verifyError();
    }
}
