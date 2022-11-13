package com.reactive.examples.service.cart;

import com.google.gson.Gson;
import com.reactive.examples.exception.NotEnoughCashException;
import com.reactive.examples.exception.NotEnoughProductAmountException;
import com.reactive.examples.model.Product;
import com.reactive.examples.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ProductRepository productRepository;
    public final Map<Product, Integer> productMap = new HashMap<>();


    public Map<Product, Integer> getProductMap(){
        return productMap;
    }
    private final BigDecimal[] coins = {
            new BigDecimal("0.50"), new BigDecimal("0.20"), new BigDecimal("0.10"),
            new BigDecimal("0.05"), new BigDecimal("0.03"), new BigDecimal("0.02"),new BigDecimal("0.01")
    };
    private final BigDecimal[] bankNotes = {
           new BigDecimal("500.00"),new BigDecimal("200.00"), new BigDecimal("100.00"), new BigDecimal("50.00"), new BigDecimal("20.00"),
            new BigDecimal("10.00"), new BigDecimal("5.00"), new BigDecimal("2.00"),
            new BigDecimal("1.00")
    };

    Gson gson = new Gson();

    @Autowired
    public ShoppingCartServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Void> addProduct(Product product) throws NotEnoughProductAmountException {

        if (productMap.containsKey(product)) {
            if (product.getAmount() > productMap.get(product)) {
                productMap.replace(product, productMap.get(product) + 1);
            } else {
                throw new NotEnoughProductAmountException();
            }

        } else {
            if (product.getAmount() > 0) {
                productMap.put(product, 1);
            }
        }
        return Mono.empty().then();
    }



    @Override
    public Mono<Void> decreaseProductAmount(Product product) {

        if (productMap.containsKey(product)) {
            if (productMap.get(product) > 1)
                productMap.replace(product, productMap.get(product) - 1);
            else if (productMap.get(product) == 1) {
                productMap.remove(product);
            }
        }
        return Mono.empty().then();
    }

    @Override
    public Mono<Void> clearShoppingCart() {
        productMap.clear();
        return Mono.empty().then();
    }

    @Override
    public Mono<Void> deleteProduct(Product product) {

        productMap.remove(product);
        return Mono.empty().then();
    }

    @Override
    public Mono<Map<String, Integer>> getShoppingCart()  {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
            final Product entryKey = entry.getKey();
            String json = gson.toJson(entryKey);
            JSONObject mJSONObject ;
            try {
                 mJSONObject = new JSONObject(json);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            map.put(mJSONObject.toString(), entry.getValue());
        }
        return Mono.just(map);
    }




    @Override
    public Mono<Void> checkoutAndUpdate(BigDecimal paidAmount) throws NotEnoughProductAmountException, NotEnoughCashException {
        BigDecimal totalPrice = getTotal();
        List<Product> products = new ArrayList<>();

                        if (totalPrice.compareTo(paidAmount) > 0) {
                            throw new NotEnoughCashException();
                        }

                for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {

                    productRepository.findById(entry.getKey().getId().intValue())
                            .subscribe(u -> {
                                if (entry.getValue() > u.getAmount()) {
                                    throw new NotEnoughProductAmountException();
                                }
                                entry.getKey().setAmount(u.getAmount() - entry.getValue());
                                products.add(entry.getKey());
                            });

                    productRepository.findAll()
                            .thenMany(Flux.fromIterable(products))
                            .flatMap(productRepository::save)
                            .thenMany(productRepository.findAll())
                            .subscribe(goods -> {
                                System.out.println("Products are updated from CommandLineRunner " + goods);
                            });


                }
        return Mono.empty().then();

    }


    @Override
    public Mono<Map<String, Integer>> returnChange(BigDecimal payment) {
        Map<String, Integer> changeReturned = new HashMap<>();
        BigDecimal totalPrice = getTotal();
            if (totalPrice.compareTo(payment) == 0) {
                changeReturned.put("no_change", 0);
            }
            BigDecimal change = payment.subtract(totalPrice);
            BigDecimal coinChange = change.remainder(BigDecimal.ONE);
            BigDecimal bankNoteChange = change.subtract(coinChange);


            Map<String, Integer> bankNotesMap = pairReturn(bankNoteChange, bankNotes);
            Map<String, Integer> coinsMap = pairReturn(coinChange, coins);

           Map<String,Integer>  changeMap = new HashMap<>();
          changeMap.putAll(bankNotesMap);
          changeMap.putAll(coinsMap);


         productMap.clear();


        return Mono.just(changeMap);


    }

    private Map<String, Integer> pairReturn(BigDecimal checkoutPrice, BigDecimal[] changeTypes) {


        Map<String, Integer> pairMap = new LinkedHashMap<>();
        for (BigDecimal change : changeTypes) {
            if (checkoutPrice.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
            BigDecimal count = checkoutPrice.divideToIntegralValue(change);
            if (count.compareTo(BigDecimal.ONE) >= 0) {
                pairMap.put(change.toString(), count.intValue());
                checkoutPrice = checkoutPrice.subtract(count.multiply(change));
            }
        }
        return pairMap;
    }

    @Override
    public BigDecimal getTotal() {
        return productMap
                .entrySet()
                .stream()
                .map(entry -> entry
                        .getKey()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}

