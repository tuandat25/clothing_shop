package com.tuandat.clothingshop.repositories;

import com.tuandat.clothingshop.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserId(UUID userId);
}
