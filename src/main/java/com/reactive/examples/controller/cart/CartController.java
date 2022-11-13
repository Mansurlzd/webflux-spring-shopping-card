package com.reactive.examples.controller.cart;


import com.reactive.examples.model.Product;
import com.reactive.examples.service.cart.ShoppingCartService;
import com.reactive.examples.service.goods.ProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;

@Api(value = "Shopping Cart", consumes = "application/json", produces = "application/json")
@RestController
@RequestMapping(value = "api/shoppingCart")
public class CartController {

    private final ShoppingCartService shoppingCartService;
    private final ProductService<Product> productService;


    @Autowired
    public CartController(ShoppingCartService shoppingCartService, ProductService<Product> productService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
    }

    @GetMapping
    public Mono<Map<String, Integer>> getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @GetMapping("/total")
    public Mono<BigDecimal> getShoppingCartTotal() {
        return Mono.just(shoppingCartService.getTotal());
    }

    @PostMapping("/{id}")
    public Mono<Map<String, Integer>> addProductToCart(@PathVariable("id") int id) {
          productService.findById(id).subscribe(v->{
              shoppingCartService.addProduct(v);
          });
          return shoppingCartService.getShoppingCart();

    }

    @DeleteMapping("/{id}")
    public Mono<Map<String, Integer>> decreaseProductAmount(@PathVariable("id") int id) {
        productService.findById(id).
                subscribe(v->{
                    shoppingCartService.decreaseProductAmount(v);
                });

        return shoppingCartService.getShoppingCart();
    }

    @DeleteMapping("/{id}/all")
    public Mono<Map<String, Integer>> deleteFromCart(@PathVariable("id") int id) {
        productService.findById(id).
                subscribe(v->{
                    shoppingCartService.deleteProduct(v);
                });
        return shoppingCartService.getShoppingCart();
    }

    @DeleteMapping
    public Mono<Map<String, Integer>> clearShoppingCart() {
        shoppingCartService.clearShoppingCart();
        return shoppingCartService.getShoppingCart();
    }

    @PostMapping("/checkout/{paidAmount}")
    public Mono<Map<String,Integer>>checkout(@Valid @PathVariable("paidAmount") BigDecimal paidAmount) {

        shoppingCartService.checkoutAndUpdate(paidAmount);
        return shoppingCartService.returnChange(paidAmount);


    }



}