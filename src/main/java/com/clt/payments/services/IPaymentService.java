package com.clt.payments.services;

import com.clt.payments.dtos.PaymentIn;
import com.clt.payments.dtos.PaymentOut;

import reactor.core.publisher.Mono;

public interface IPaymentService {

    Mono<PaymentOut> executePayment(PaymentIn serviceIn);
    
}
