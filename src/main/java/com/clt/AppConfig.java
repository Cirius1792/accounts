package com.clt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.clt.configuration.AccountsProperties;

@Configuration
public class AppConfig {
    
    @Bean
    public AccountsProperties accountProperties(@Value("${accountId}") Long accountId){
        return new AccountsProperties(accountId);
    }
}
