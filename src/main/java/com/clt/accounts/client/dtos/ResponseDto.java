package com.clt.accounts.client.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto<T> {
    @Data
    @NoArgsConstructor
    public class Error {
        String code;
        String description;
        String params;
    }

    String status;
    List<Error> errors;
    T payload;
}
