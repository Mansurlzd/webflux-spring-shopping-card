package com.reactive.examples.service.goods;


import com.reactive.examples.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface ProductService<T extends Product> {
    Mono<T> findById(int id);

    Mono<Void> save(Product product);
}
