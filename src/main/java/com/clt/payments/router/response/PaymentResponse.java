package com.clt.payments.router.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PaymentResponse {
    String moneyTransferId;
    String status;
}
