package com.reactive.examples.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @Column(unique = true,nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false,columnDefinition = "DECIMAL(3,2)")
    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal price;

    @Column(nullable = false)
    @NotNull
    @Min(value = 0)
    private long amount;

    @Column(nullable = false)
    @NotBlank
    private String imagePath;





}
