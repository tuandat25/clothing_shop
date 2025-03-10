package com.tuandat.clothingshop.repositories;

import com.tuandat.clothingshop.models.Category;
import com.tuandat.clothingshop.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    List<OrderDetail> findByOrderId(long orderId);
}
