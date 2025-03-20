package com.tuandat.clothingshop.models;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product_variants")
public class ProductVariant {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String model; // mẫu (ví dụ: "dài")
    private String size;  // kích cỡ (ví dụ: "M", "XL")
    private int quantity; // số lượng hàng

}

