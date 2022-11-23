package com.clt.accounts.handlers.responses;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionsResponse extends BaseResponse{
    List<TransactionResponse> transactions;

}
