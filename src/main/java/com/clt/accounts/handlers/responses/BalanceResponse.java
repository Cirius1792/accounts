package com.clt.accounts.handlers.responses;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceResponse extends BaseResponse {
    Date date;
    BigDecimal availableBalance;
    BigDecimal balance;
    String currency;
}
