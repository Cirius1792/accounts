package com.clt.accounts.router.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public abstract class BaseResponse {

    @Data
    class Error {
        String code;
        String description;
    }
    
    @JsonInclude(Include.NON_NULL)
    Error error;

}
