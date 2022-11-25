package com.clt;

import java.time.Clock;

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
public class AppConfig {


    @Bean
    public AccountsClient accountsClient(@Value("${clients.accounts.endpoint}") String endpoint, @Value("${clients.apiKey}") String apiKey){
        return new AccountsClientImpl(endpoint, apiKey);
    }

    @Bean
    public AccountComponent accountService(@Value("${accountId}") Long accountId, AccountsClient AccountsClient){
        return new AccountComponentImpl(accountId, AccountsClient);
    }

    @Bean
    public RouterFunction<ServerResponse> accountApis(AccountComponent accountService){
        return new AccountRouter(accountService, Clock.systemDefaultZone()).accountApis();
    }
}
