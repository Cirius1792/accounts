package com.clt.accounts.service;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {
    Long transactionId;
    String operationId;
    Date accountingDate;
    Date valueDate;
    BigDecimal amount;
    String currency;
    String description;
}
