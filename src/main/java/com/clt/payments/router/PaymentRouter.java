package com.clt.payments.router;

import com.clt.common.router.response.ErrorResponse;
import com.clt.payments.component.PaymentComponent;
import com.clt.payments.router.request.PaymentRequestDto;
import com.clt.payments.router.response.PaymentResponseDto;

import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
public class PaymentRouter {

    final PaymentComponent paymentComponent;

    public PaymentRouter(PaymentComponent paymentComponent) {
        this.paymentComponent = paymentComponent;
    }

    Mono<ServerResponse> postPayment(ServerRequest request) {
        return request.bodyToMono(PaymentRequestDto.class)
                .map(PaymentRequestDto::fromDto)
                .flatMap(el -> ServerResponse.ok()
                        .body(paymentComponent.executePayment(el)
                                        .map(out -> PaymentResponseDto.builder()
                                                .moneyTransferId(out.getMoneyTransferId())
                                                .status(out.getStatus())
                                                .build()),
                                PaymentResponseDto.class));
    }

    public RouterFunction<ServerResponse> paymentApis() {
        return route()
                .POST("/payment", accept(MediaType.APPLICATION_JSON), this::postPayment, ops ->
                        ops.operationId("executePayment")
                                .description("Execute a payment")
                                .tag("Payments")
                                .requestBody(requestBodyBuilder().implementation(PaymentRequestDto.class))
                                .response(responseBuilder()
                                        .responseCode("200")
                                        .description("Payment request created successfully")
                                        .implementation(PaymentResponseDto.class))
                                .response(responseBuilder()
                                        .responseCode("400")
                                        .description("There is an error in the request parameters")
                                        .implementation(ErrorResponse.class))
                                .response(responseBuilder()
                                        .responseCode("500")
                                        .description("Internal Server Error")
                                        .implementation(ErrorResponse.class)))
                .build();
    }
}
