package com.clt.accounts.component;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.clt.accounts.client.AccountsClient;
import com.clt.accounts.client.dto.BalanceDto;
import com.clt.accounts.client.dto.TransactionDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class AccountComponentImplTest {

        Long accountId = 999L;

        @Test
        void testInitAccountServiceNoAccount() {
                Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new AccountComponentImpl(null, mock(AccountsClient.class)),
                                "Should throw because of missing account number");
        }

        @Test
        void testRetrieveBalanceOk() throws ParseException {
                String dateString = "2022-12-31";
                LocalDate date = LocalDate.parse(dateString);
                BigDecimal balance = BigDecimal.valueOf(99.99);
                BigDecimal availableBalance = BigDecimal.valueOf(98.88);
                String currency = "EUR";

                BalanceEntity expected = BalanceEntity.builder()
                                .availableBalance(availableBalance)
                                .balance(balance)
                                .currency(currency)
                                .date(date)
                                .build();
                BalanceDto dto = BalanceDto.builder()
                                .availableBalance(availableBalance)
                                .balance(balance)
                                .currency(currency)
                                .date(dateString)
                                .build();

                AccountsClient client = mock(AccountsClient.class);
                when(client.retrieveBalance(accountId)).thenReturn(Mono.just(dto));

                AccountComponent service = new AccountComponentImpl(accountId, client);
                Mono<BalanceEntity> actual = service.retrieveBalance();

                StepVerifier.create(actual)
                                .expectNext(expected)
                                .verifyComplete();
        }

        @Test
        void testRetrieveTransactionsOk() throws ParseException {
                AccountsClient client = mock(AccountsClient.class);
                AccountComponent service = new AccountComponentImpl(accountId, client);

                String transactionDateString = "2022-01-02";
                LocalDate transactionDate = LocalDate.parse(transactionDateString);

                String currency = "EUR";

                String description1 = "Gift";
                BigDecimal amount1 = BigDecimal.valueOf(-10L);

                String description2 = "Gifted";
                BigDecimal amount2 = BigDecimal.valueOf(100L);

                TransactionDto transaction1 = TransactionDto.builder()
                                .amount(amount1)
                                .accountingDate(transactionDateString)
                                .currency(currency)
                                .description(description1)
                                .build();
                TransactionDto transaction2 = TransactionDto.builder()
                                .amount(amount2)
                                .accountingDate(transactionDateString)
                                .currency(currency)
                                .description(description2)
                                .build();

                TransactionEntity expectedTransaction1 = TransactionEntity.builder()
                                .amount(amount1)
                                .accountingDate(transactionDate)
                                .currency(currency)
                                .description(description1)
                                .build();
                TransactionEntity expectedTransaction2 = TransactionEntity.builder()
                                .amount(amount2)
                                .accountingDate(transactionDate)
                                .currency(currency)
                                .description(description2)
                                .build();

                when(client.retrieveTransactions(eq(accountId), any(), any()))
                                .thenReturn(Flux.fromIterable(Arrays.asList(transaction1, transaction2)));

                Flux<TransactionEntity> actual = service.retrieveTransactions(transactionDate, transactionDate);
                StepVerifier.create(actual)
                                .expectNext(expectedTransaction1)
                                .expectNext(expectedTransaction2)
                                .verifyComplete();
        }

    @Test
    void testRetrieveTransactionsWrongDates(){
        AccountsClient client = mock(AccountsClient.class);
        AccountComponent service = new AccountComponentImpl(accountId, client);
        Flux<TransactionEntity> result = service.retrieveTransactions(LocalDate.parse("2022-01-31"), LocalDate.parse("2022-01-30"));
        StepVerifier.create(result)
        .expectError(IllegalArgumentException.class);
    }
    
    @Test
    void testRetrieveTransactionsMissingDates(){
        AccountsClient client = mock(AccountsClient.class);
        AccountComponent service = new AccountComponentImpl(accountId, client);
        
        Flux<TransactionEntity> result = service.retrieveTransactions(null, LocalDate.parse("2022-01-30"));
        StepVerifier.create(result)
        .expectError(IllegalArgumentException.class);

        result = service.retrieveTransactions(LocalDate.parse("2022-01-30"), null);
        StepVerifier.create(result)
        .expectError(IllegalArgumentException.class);
    }
}
