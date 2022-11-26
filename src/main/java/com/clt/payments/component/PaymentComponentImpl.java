package com.clt.payments.component;

import java.math.BigDecimal;

import com.clt.payments.client.dto.AccountDto;
import com.clt.payments.client.dto.CreditorDto;
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
    public Mono<PaymentReceiptEntity> executePayment(PaymentEntity paymentEntity) {
        if (paymentEntity.getAmount() == null || BigDecimal.ZERO.equals(paymentEntity.getAmount()))
            return Mono.error(new IllegalArgumentException("Invalid Amount"));
        if (StringUtils.isBlank(paymentEntity.getCurrency()))
            return Mono.error(new IllegalArgumentException("Missing Currency"));
        if (StringUtils.isBlank(paymentEntity.getReceiverAccount()))
            return Mono.error(new IllegalArgumentException("Missing target account details"));

        return paymentClient.postPayment(this.accountNumber, PaymentRequestDto.builder()
                        .creditor(CreditorDto.builder()
                                .name(paymentEntity.getReceiverName())
                                .account(AccountDto.builder().accountCode(paymentEntity.getReceiverAccount()).build())
                                .build())
                        .amount(paymentEntity.getAmount())
                        .currency(paymentEntity.getCurrency())
                        .description(paymentEntity.getDescription())
                        .build())
                .map(response -> PaymentReceiptEntity.builder()
                        .direction(response.getDirection())
                        .moneyTransferId(response.getMoneyTransferId())
                        .status(response.getStatus())
                        .build());
    }

}
