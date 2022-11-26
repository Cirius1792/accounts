package com.clt.payments.client.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequestDto {
    CreditorDto creditor;
    String description;
    String currency;
    BigDecimal amount;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate executionDate;
}
