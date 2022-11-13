package com.reactive.examples.exception;

public class NotEnoughProductAmountException extends RuntimeException {
    public NotEnoughProductAmountException() {
        super("Not enough amount of product found in the stock");
    }
}
