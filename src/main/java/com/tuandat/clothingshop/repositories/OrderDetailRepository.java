package com.tuandat.clothingshop.repositories;

import com.tuandat.clothingshop.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    List<OrderDetail> findByOrderId(UUID id);
}
