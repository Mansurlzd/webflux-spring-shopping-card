package com.reactive.examples.service.goods;


import com.reactive.examples.model.Product;
import com.reactive.examples.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class ProductServiceImpl implements ProductService<Product> {
    private final ProductRepository productRepository;
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Product> findById(int id) {
        return productRepository.findById( id);
    }
    @Override
    public Mono<Void> save(Product product) {
         productRepository.save(product);
         return Mono.empty().then();
    }

}
