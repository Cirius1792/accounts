package com.clt.accounts.client.dtos;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BalanceDto {
    public static String DATE_FORMAT = "yyyy-MM-dd";
    Date date;
    BigDecimal availableBalance;
    BigDecimal balance;
    String currency;
    
    
    
    @Builder
    public BalanceDto(String date, BigDecimal availableBalance, BigDecimal balance, String currency) {
        this.setDate(date);
        this.availableBalance = availableBalance;
        this.balance = balance;
        this.currency = currency;
    }



    void setDate(String date){
        try {
            this.date = new SimpleDateFormat(DATE_FORMAT).parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Date format not supported for date: " + date);
        }
    }
}
