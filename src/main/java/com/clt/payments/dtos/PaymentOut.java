package com.clt.payments.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentOut {
    String moneyTransferId;
    String status;
    String direction;
}
