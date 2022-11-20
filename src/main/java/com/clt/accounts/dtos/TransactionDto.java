package com.clt.accounts.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TransactionDto {
    Long transactionId;
    String operationId;
    String accountingDate; // TODO: make it a date
    String valueDate; // TODO: make it a date
    BigDecimal amount;
    String currency;
    String description;
}
