package com.reactive.examples.initialize;

import com.reactive.examples.model.Product;
import com.reactive.examples.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class ProductInitializer implements CommandLineRunner {


    private  final ProductRepository productRepository;


    @Override
    public void run(String... args) {
        initialDataSetup();
    }

    private List<Product> createProducts() {

        return List.of(
                Product.builder().name("Brownie")
                        .amount(48)
                        .price(new BigDecimal("0.65"))
                        .imagePath("./images/brownie.jpeg").build(),
                Product.builder().name("Muffin")
                        .amount(36)
                        .price(new BigDecimal("1.00"))
                        .imagePath("./images/muffin.jpeg").build(),
                Product.builder().name("Cake Pop")
                        .amount(24)
                        .price(new BigDecimal("1.35"))
                        .imagePath("./images/cakepop.jpeg").build(),
                Product.builder().name("Apple Tort")
                        .amount(60)
                        .price(new BigDecimal("1.50"))
                        .imagePath("./images/appletort.jpeg").build(),
                Product.builder().name("Water")
                        .amount(30)
                        .price(new BigDecimal("1.50"))
                        .imagePath("./images/water.jpeg").build(),
                Product.builder().name("Shirts")
                        .amount(5)
                        .price(new BigDecimal("3.00"))
                        .imagePath("./images/shirts.jpeg").build(),
                Product.builder().name("Pants")
                        .amount(15)
                        .price(new BigDecimal("2.00"))
                        .imagePath("./images/pants.jpeg").build(),
                Product.builder().name("Jacket")
                        .amount(5)
                        .price(new BigDecimal("4.00"))
                        .imagePath("./images/jacket.jpeg").build(),
                Product.builder().name("Car")
                        .amount(5)
                        .price(new BigDecimal("1.00"))
                        .imagePath("./images/toy.jpeg").build());

    }




    private void initialDataSetup() {

        productRepository.findAll()
                .thenMany(Flux.fromIterable(createProducts()))
                .flatMap(productRepository::save)
                .thenMany(productRepository.findAll())
                .subscribe(goods -> {
                    log.info("Products are inserted from CommandLineRunner " + goods);
                });



    }

}
