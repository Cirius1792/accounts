package com.clt.common.error;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import lombok.extern.slf4j.Slf4j;

record ExceptionRule(Class<?> exceptionClass, HttpStatus status) {
}

@Component
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes {
        final static String INTERNAL_SERVER_ERROR_MESSAGE = "An error occurred while serving the request";

        private final List<ExceptionRule> exceptionsRules = List.of(
                        new ExceptionRule(IllegalArgumentException.class, HttpStatus.BAD_REQUEST));
        private final ExceptionRule externalServiceErrorRule = new ExceptionRule(ExternalServiceError.class, HttpStatus.INTERNAL_SERVER_ERROR);
        @Override
        public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
                Throwable error = getError(request);
                log.error("Intercepted error", error);
                // Handle External service error in a tailored way to manage the original error code and description
                if(externalServiceErrorRule.exceptionClass().isInstance(error)){
                        ExternalServiceError errorInstance = ((ExternalServiceError) error);
                        return Map.of(
                                ErrorAttributesKey.STATUS_CODE.getKey(), errorInstance.getStatusCode(),
                                ErrorAttributesKey.CODE.getKey(), errorInstance.getMessage(),
                                ErrorAttributesKey.DESCRIPTION.getKey(), errorInstance.getDescription()
                                );
                }
                Optional<ExceptionRule> exceptionRuleOptional = exceptionsRules.stream()
                                .map(exceptionRule -> exceptionRule.exceptionClass().isInstance(error) ? exceptionRule
                                                : null)
                                .filter(Objects::nonNull)
                                .findFirst();

                return exceptionRuleOptional
                                .<Map<String, Object>>map(exceptionRule -> Map.of(
                                                ErrorAttributesKey.STATUS_CODE.getKey(), exceptionRule.status().value(),
                                                ErrorAttributesKey.DESCRIPTION.getKey(), error.getMessage()))
                                .orElseGet(() -> Map.of(
                                                ErrorAttributesKey.STATUS_CODE.getKey(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                ErrorAttributesKey.DESCRIPTION.getKey(), error.getMessage()));
        }

}
