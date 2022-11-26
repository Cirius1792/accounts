package com.clt;

import java.time.Clock;
import java.time.ZoneId;

import com.clt.payments.client.PaymentClient;
import com.clt.payments.client.PaymentClientImpl;
import com.clt.payments.component.PaymentComponent;
import com.clt.payments.component.PaymentComponentImpl;
import com.clt.payments.router.PaymentRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.clt.accounts.client.AccountsClient;
import com.clt.accounts.client.AccountsClientImpl;
import com.clt.accounts.component.AccountComponent;
import com.clt.accounts.component.AccountComponentImpl;
import com.clt.accounts.router.AccountRouter;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    @Qualifier("accountId")
    public Long accountId(@Value("${accountId}") Long accountId){
        log.info("Configuring application using account id: {}", accountId);
        return accountId;
    }


    @Bean
    public AccountsClient accountsClient(@Value("${clients.platform.endpoint}") String endpoint, @Value("${clients.apiKey}") String apiKey){
        return new AccountsClientImpl(endpoint, apiKey);
    }

    @Bean
    public AccountComponent accountService(Long accountId, AccountsClient AccountsClient){
        return new AccountComponentImpl(accountId, AccountsClient);
    }

    @Bean
    public RouterFunction<ServerResponse> accountApis(AccountComponent accountService){
        return new AccountRouter(accountService, Clock.systemDefaultZone()).accountApis();
    }

    @Bean
    public PaymentClient paymentClient(@Value("${clients.platform.endpoint}") String endpoint, @Value("${clients.apiKey}") String apiKey){
        return new PaymentClientImpl(endpoint, apiKey, ZoneId.systemDefault().toString());
    }

    @Bean
    public PaymentComponent paymentComponent(Long accountId, PaymentClient paymentClient){
        return new PaymentComponentImpl(accountId, paymentClient);
    }

    @Bean
    public RouterFunction<ServerResponse> paymentApis(PaymentComponent paymentComponent){
        return new PaymentRouter(paymentComponent).paymentApis();
    }
}
