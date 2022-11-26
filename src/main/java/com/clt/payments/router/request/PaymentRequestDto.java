package com.clt.payments.router.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.clt.payments.component.PaymentEntity;
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

    public static PaymentEntity fromDto( PaymentRequestDto paymentRequest){
        return PaymentEntity.builder()
                .currency(paymentRequest.getCurrency())
                .description(paymentRequest.getDescription())
                .executionDate(paymentRequest.getExecutionDate())
                .receiverAccount(paymentRequest.getReceiverAccount())
                .receiverName(paymentRequest.getReceiverName())
                .amount(paymentRequest.getAmount())
                .build();
    }
}
