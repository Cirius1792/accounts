package com.clt.common.client;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Error {
    String code;
    String description;
    String params;
}