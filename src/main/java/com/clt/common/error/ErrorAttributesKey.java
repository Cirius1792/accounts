package com.clt.common.error;

import lombok.Getter;

@Getter
enum ErrorAttributesKey {
    CODE("code"),
    DESCRIPTION("description");

    private final String key;

    ErrorAttributesKey(String key) {
        this.key = key;
    }
}