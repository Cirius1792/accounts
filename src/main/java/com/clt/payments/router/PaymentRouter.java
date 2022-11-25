package com.clt.payments.router;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.clt.payments.component.PaymentComponent;
import com.clt.payments.component.PaymentIn;
import com.clt.payments.router.request.PaymentRequest;
import com.clt.payments.router.response.PaymentResponse;

import reactor.core.publisher.Mono;

public class PaymentRouter {

    final PaymentComponent paymentComponent;

    public PaymentRouter(PaymentComponent paymentComponent) {
        this.paymentComponent = paymentComponent;
    }

    Mono<ServerResponse> postPayment(ServerRequest request) {
        return request.bodyToMono(PaymentRequest.class)
                .map(el -> PaymentIn.builder()
                        .currency(el.getCurrency())
                        .description(el.getDescription())
                        .executionDate(el.getExecutionDate())
                        .receiverAccount(el.getReceiverAccount())
                        .receiverName(el.getReceiverName())
                        .amount(el.getAmount())
                        .build())
                .flatMap(el -> ServerResponse.ok()
                        .body(paymentComponent.executePayment(el)
                                .map(out -> PaymentResponse.builder()
                                        .moneyTransferId(out.getMoneyTransferId())
                                        .status(out.getStatus())
                                        .build()),
                                PaymentResponse.class));
    }

    public RouterFunction<ServerResponse> paymentApis() {
        return RouterFunctions.route()
                .POST("/payment", this::postPayment)
                .build();
    }
}
