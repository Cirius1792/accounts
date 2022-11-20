package com.clt.accounts.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionsDto {
    List<TransactionDto> list;
}
