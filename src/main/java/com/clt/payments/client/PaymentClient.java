package com.clt.payments.client;

import com.clt.payments.client.dto.PaymentRequestDto;
import com.clt.payments.client.dto.PaymentResponseDto;

import reactor.core.publisher.Mono;

public interface PaymentClient {
    Mono<PaymentResponseDto> postPayment(Long accountId, PaymentRequestDto request);
}
