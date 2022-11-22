package com.clt.accounts.routers;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.net.http.WebSocket.Builder;
import java.util.function.Consumer;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.clt.accounts.handlers.AccountsHandler;
import com.clt.accounts.handlers.responses.BalanceResponse;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;

public class AccountsRouter {

    AccountsHandler handler;

    RouterFunction<ServerResponse> accountRoutes() {
        return route()
                .GET("/accounts",
                        req -> ok().contentType(MediaType.APPLICATION_JSON)
                                .body(handler.getBalance(), BalanceResponse.class),
                        ops -> ops.operationId("getBalance")
                                .response(responseBuilder()
                                        .responseCode("200")
                                        .description("User Balance")))
                .build();
    }

}
