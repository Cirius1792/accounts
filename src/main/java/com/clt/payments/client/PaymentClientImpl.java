package com.clt.payments.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import com.clt.payments.client.dto.PaymentRequestDto;
import com.clt.payments.client.dto.PaymentResponseDto;
import com.clt.payments.client.dto.ResponseDto;

import reactor.core.publisher.Mono;

class PaymentClientImpl implements PaymentClient {

    final String PAYMENT_ENDPOINT = "/api/gbs/banking/v4.0/accounts/{accountId}/payments/money-transfers";

    static final ParameterizedTypeReference<ResponseDto<PaymentResponseDto>> typeReferencePaymentResponse = new ParameterizedTypeReference<ResponseDto<PaymentResponseDto>>() {
    };
    static final String AUTH_SCHEMA = "S2S";
    final String basePath;
    final WebClient client;

    public PaymentClientImpl(String basePath, String apiKey, String zoneId) {
        this.basePath = basePath;
        this.client = WebClient.builder()
                .defaultHeader("Api-Key", apiKey)
                .defaultHeader("Auth-Schema", AUTH_SCHEMA)
                .defaultHeader("X-Time-Zone", zoneId)
                .baseUrl(this.basePath)
                .build();
    }

    @Override
    public Mono<PaymentResponseDto> postPayment(String accountId, PaymentRequestDto request) {
        return this.client.post()
                .uri(PAYMENT_ENDPOINT, accountId)
                .body(Mono.just(request), PaymentRequestDto.class)
                .retrieve()
                .bodyToMono(typeReferencePaymentResponse)
                .map(el -> el.getPayload());
    }

}