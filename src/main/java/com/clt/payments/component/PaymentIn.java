package com.clt.payments.component;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PaymentIn {
    String receiverName;
    String receiverAccount;
    String description;
    String currency;
    BigDecimal amount;
    LocalDate executionDate;
}
