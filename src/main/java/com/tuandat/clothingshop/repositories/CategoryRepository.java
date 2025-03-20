package com.tuandat.clothingshop.repositories;

import com.tuandat.clothingshop.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
//    List<OrderDetail> findByOrderId(long orderId);
//    List<OrderDetail> findByProductId(long productId);
    UUID findIdByName(String name);
}
