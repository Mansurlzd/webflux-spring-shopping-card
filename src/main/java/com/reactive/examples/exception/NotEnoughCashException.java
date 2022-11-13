package com.reactive.examples.exception;

public class NotEnoughCashException extends RuntimeException {
    public NotEnoughCashException () {
        super("Not enough cash to proceed.");
    }
}
