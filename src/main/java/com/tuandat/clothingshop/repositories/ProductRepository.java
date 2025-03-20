package com.tuandat.clothingshop.repositories;

import com.tuandat.clothingshop.models.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
        boolean existsByName(String name);

        Page<Product> findAll(Pageable pageable);

        @Query(value = "SELECT * FROM products p WHERE " +
                        "(:categoryId IS NULL OR p.category_id = :categoryId) " +
                        "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE CONCAT('%', :keyword, '%') OR p.description LIKE CONCAT('%', :keyword, '%'))", nativeQuery = true)
        Page<Product> searchProducts(@Param("categoryId") UUID categoryId,
                        @Param("keyword") String keyword, Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
        List<Product> findProductsByIds(@Param("productIds") List<UUID> productIds);
}
