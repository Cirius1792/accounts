package com.clt.accounts.handlers.responses;

import lombok.Data;

@Data
public class BaseResponse {

    @Data
    class Error {
        String code;
        String description;
    }

    Error error;

}
