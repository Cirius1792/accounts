package com.clt.payments.component;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.clt.payments.client.PaymentClient;
import com.clt.payments.client.dto.PaymentRequestDto;

import reactor.core.publisher.Mono;

public class PaymentComponentImpl implements PaymentComponent {

    final PaymentClient paymentClient;
    final Long accountNumber;

    public PaymentComponentImpl(Long accountNumber, PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
        this.accountNumber = accountNumber;
    }

    @Override
    public Mono<PaymentOut> executePayment(PaymentIn serviceIn) {
        if(serviceIn.getAmount() == null || BigDecimal.ZERO.equals(serviceIn.getAmount()))
            return Mono.error(new IllegalArgumentException("Invalid Amount"));
        if(StringUtils.isBlank(serviceIn.getCurrency()))
            return Mono.error(new IllegalArgumentException("Missing Currency"));
        if(StringUtils.isBlank(serviceIn.getReceiverAccount()))
            return Mono.error(new IllegalArgumentException("Missing target account details"));

        return paymentClient.postPayment(this.accountNumber, PaymentRequestDto.builder()
                .receiverName(serviceIn.getReceiverName())
                .amount(serviceIn.getAmount())
                .currency(serviceIn.getCurrency())
                .description(serviceIn.getDescription())
                .build()).map(
                        response -> PaymentOut.builder()
                        .direction(response.getDirection())
                        .moneyTransferId(response.getMoneyTransferId())
                        .status(response.getStatus())        
                        .build());
    }

}
