package com.clt.payments.client.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequestDto {
    String receiverName;
    String description;
    String currency;
    BigDecimal amount;
    LocalDate executionDate;
}
