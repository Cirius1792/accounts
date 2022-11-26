package com.clt.payments.client.dto;

import com.clt.common.client.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto extends ResponseDto<PaymentResponseDto> {
    String moneyTransferId;
    String status;
    String direction;
}
