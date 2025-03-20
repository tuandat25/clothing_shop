package com.tuandat.clothingshop.responses;

import com.tuandat.clothingshop.models.Order;
import com.tuandat.clothingshop.models.OrderDetail;
import com.tuandat.clothingshop.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderDetailResponse {
    private UUID id;

    @JsonProperty("order_id")
    private UUID orderId;

    @JsonProperty("product_id")
    private UUID productId;

    private Float price;

    @JsonProperty("number_of_products")
    private long numberOfProducts;

    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .color(orderDetail.getColor())
                .price(orderDetail.getPrice())
                .totalMoney(orderDetail.getTotalMoney())
                .build();
    }
}
