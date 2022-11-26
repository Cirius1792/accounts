package com.clt.payments.router.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    String receiverName;
    @NotBlank(message = "The receiver account identifier is required")
    String receiverAccount;
    String description;
    @NotNull
    LocalDate executionDate;
    @NotBlank(message = "The currency is required")
    String currency;
    @NotNull(message = "The amount is required")
    BigDecimal amount;
}
