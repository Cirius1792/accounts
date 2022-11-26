package com.clt.payments.component;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentReceiptEntity {
    String moneyTransferId;
    String status;
    String direction;
}
