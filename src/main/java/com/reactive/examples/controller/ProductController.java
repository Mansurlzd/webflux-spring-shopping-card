package com.reactive.examples.controller;

import com.reactive.examples.model.Product;
import com.reactive.examples.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;


@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Flux<Product> findAllProducts() {

        Flux<Product> goodsFlux = productRepository.findAll();
        goodsFlux.sort(Comparator.comparing(Product::getId));
        return goodsFlux;
    }


    @GetMapping("/{id}")
    Mono<ResponseEntity<Product>> findProductById(@PathVariable int id) {
        Mono<Product> product = productRepository.findById( id);
        return product.map( p -> ResponseEntity.ok().body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    Mono<ResponseEntity<Product>> updateProduct(@PathVariable int id,  @RequestBody Product product) {
        return productRepository.findById( id).map(existingGoods->{
                    existingGoods.setName(product.getName());
                    existingGoods.setImagePath(product.getImagePath());
                    existingGoods.setAmount(product.getAmount());
                    existingGoods.setPrice(product.getPrice());
                    return existingGoods;
                }).flatMap(existing->productRepository.save(existing))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    void deleteProductById(@PathVariable int id) {
        productRepository.deleteById( id);
    }

    @DeleteMapping
    void deleteAllProducts() {
        productRepository.deleteAll();
    }

}
