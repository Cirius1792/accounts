package com.clt.payments.component;

import reactor.core.publisher.Mono;

public interface IPaymentService {

    Mono<PaymentOut> executePayment(PaymentIn serviceIn);
    
}
