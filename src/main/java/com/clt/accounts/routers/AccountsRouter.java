package com.clt.accounts.routers;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.clt.accounts.handlers.AccountsHandler;
import com.clt.accounts.handlers.responses.BalanceResponse;
import com.clt.accounts.handlers.responses.TransactionsResponse;

public class AccountsRouter {

        AccountsHandler handler;

        RouterFunction<ServerResponse> accountRoutes() {
                return route()
                                .GET("/account",
                                                req -> ok().contentType(MediaType.APPLICATION_JSON)
                                                                .body(handler.getBalance(), BalanceResponse.class),
                                                ops -> ops.operationId("getBalance")
                                                                .response(responseBuilder()
                                                                                .responseCode("200")
                                                                                .description("User Balance")))
                                .GET("/account/transactions",
                                                req -> ok().contentType(MediaType.APPLICATION_JSON)
                                                                .body(handler.getTransactions(
                                                                                req.queryParam("dateFrom").orElse(""),
                                                                                req.queryParam("dateTo").orElse("")),
                                                                                TransactionsResponse.class),
                                                ops -> ops.operationId("getTransactions")
                                                                .parameter(parameterBuilder()
                                                                                .name("fromDate")
                                                                                .implementation(String.class))
                                                                .parameter(parameterBuilder()
                                                                                .name("toDate")
                                                                                .implementation(String.class))
                                                                .response(responseBuilder()
                                                                                .responseCode("200")
                                                                                .description("Retrieve user transactions")))
                                .build();
        }

}
