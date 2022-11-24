package com.clt.accounts.router.responses;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceResponse extends BaseResponse {
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate date;
    BigDecimal availableBalance;
    BigDecimal balance;
    String currency;
}
