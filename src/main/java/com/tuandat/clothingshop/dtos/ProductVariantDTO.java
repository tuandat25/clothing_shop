package com.tuandat.clothingshop.dtos;

import java.util.UUID;

import com.tuandat.clothingshop.models.Product;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class ProductVariantDTO {
    private UUID id;

    private Product product;

    private UUID productId;
    private String model; // mẫu (ví dụ: "dài")
    private String size;  // kích cỡ (ví dụ: "M", "XL")
    private int quantity; // số lượng hàng
}
