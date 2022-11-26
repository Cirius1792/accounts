package com.clt.payments.component;

import reactor.core.publisher.Mono;

public interface PaymentComponent {

    Mono<PaymentReceiptEntity> executePayment(PaymentEntity serviceIn);
    
}
