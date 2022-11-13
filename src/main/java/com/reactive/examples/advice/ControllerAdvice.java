package com.reactive.examples.advice;


import com.reactive.examples.exception.NotEnoughCashException;
import com.reactive.examples.exception.NotEnoughProductAmountException;
import com.reactive.examples.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionMessage> productNotFoundException(ProductNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionMessage("PRODUCT_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(NotEnoughCashException.class)
    public ResponseEntity<ExceptionMessage> notEnoughCashException (NotEnoughCashException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionMessage("NOT_ENOUGH_CASH_YOU_PROVIDED", e.getMessage()));
    }

    @ExceptionHandler(NotEnoughProductAmountException.class)
    public ResponseEntity<ExceptionMessage> notEnoughProductAmountException(NotEnoughProductAmountException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionMessage("NOT_ENOUGH_PRODUCT_AT_STOCK", e.getMessage()));
    }

}
