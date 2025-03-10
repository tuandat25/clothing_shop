package com.tuandat.clothingshop.repositories;

import com.tuandat.clothingshop.models.Product;
import com.tuandat.clothingshop.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProductId(Long id);
}
