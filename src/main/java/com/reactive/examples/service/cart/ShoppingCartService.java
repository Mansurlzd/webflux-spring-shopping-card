package com.reactive.examples.service.cart;


import com.reactive.examples.exception.NotEnoughProductAmountException;
import com.reactive.examples.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Service
public interface ShoppingCartService  {
    Mono<Void> addProduct(Product product);
    Mono<Void> decreaseProductAmount(Product product);
    Mono<Void> deleteProduct(Product product);
    BigDecimal getTotal();
    Mono<Void> checkoutAndUpdate(BigDecimal paidAmount) throws NotEnoughProductAmountException;

    Mono<Void> clearShoppingCart();

    Map<Product, Integer> getProductMap();
    Mono<Map<String, Integer>> getShoppingCart();

    Mono<Map<String, Integer>> returnChange(BigDecimal cashInserted);
}
