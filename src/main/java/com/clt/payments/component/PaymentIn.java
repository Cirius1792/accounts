package com.clt.payments.component;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class PaymentIn {
    Long accountId;
    String receiverName;
    String description;
    String currency;
    BigDecimal amount;
    Date executionDate;
}
