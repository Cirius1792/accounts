package com.clt.accounts.router.responses;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionsResponse extends BaseResponse{
    List<TransactionResponse> transactions;

}
