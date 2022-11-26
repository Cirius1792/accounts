package com.clt.accounts.router.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponseDto {
    Long transactionId;
    String operationId;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate accountingDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate valueDate;
    BigDecimal amount;
    String currency;
    String description;
    
}
