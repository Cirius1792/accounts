package com.clt.payments.component;

import reactor.core.publisher.Mono;

/**
 * Interface defining the method used to invoice a payment from a fixed account number
 */
public interface PaymentComponent {

    Mono<PaymentReceiptEntity> executePayment(PaymentEntity serviceIn);

}
