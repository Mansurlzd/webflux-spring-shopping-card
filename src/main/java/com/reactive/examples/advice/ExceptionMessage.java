package com.reactive.examples.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionMessage {
    private final String code;
    private final String message;
}
