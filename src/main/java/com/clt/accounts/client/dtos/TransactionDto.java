package com.clt.accounts.client.dtos;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionDto {
    public final static String DATE_FORMAT = "yyyy-MM-dd";

    Long transactionId;
    String operationId;
    Date accountingDate;
    Date valueDate;
    BigDecimal amount;
    String currency;
    String description;

    @Builder
    public TransactionDto(Long transactionId, String operationId, String accountingDate, String valueDate,
            BigDecimal amount, String currency, String description) {
        this.transactionId = transactionId;
        this.operationId = operationId;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.setValueDate(valueDate);
        this.setAccountingDate(accountingDate);
    }

    void setAccountingDate(String accountingDate) {
        if (StringUtils.isNotBlank(accountingDate)) {
            try {
                this.accountingDate = new SimpleDateFormat(DATE_FORMAT).parse(accountingDate);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Date format not supported for date: " + accountingDate);
            }
        }
    }

    void setValueDate(String valueDate) {
        if (StringUtils.isNotBlank(valueDate)) {
            try {
                this.valueDate = new SimpleDateFormat(DATE_FORMAT).parse(valueDate);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Date format not supported for date: " + valueDate);
            }
        }
    }
}
