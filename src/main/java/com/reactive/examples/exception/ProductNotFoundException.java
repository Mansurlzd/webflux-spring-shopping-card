package com.reactive.examples.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(int id) {
        super(String.format("Product with ID of %d is not found!", id));
    }
}
