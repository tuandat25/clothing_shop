package com.tuandat.clothingshop.services.product;

import com.tuandat.clothingshop.dtos.ProductDTO;
import com.tuandat.clothingshop.dtos.ProductImageDTO;
import com.tuandat.clothingshop.exception.DataNotFoundException;
import com.tuandat.clothingshop.models.Product;
import com.tuandat.clothingshop.models.ProductImage;
import com.tuandat.clothingshop.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.*;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;

    Product getProductById(UUID id) throws Exception;

    Page<ProductResponse> getAllProducts(String keyword,
                                         UUID categoryId, PageRequest pageRequest);

    Product updateProduct(UUID id, ProductDTO dto);

    void deleteProduct(UUID id);

    boolean existByName(String name);

    ProductImage createProductImage(Product product, ProductImageDTO dto);

    void isValidSize(UUID productId);

    List<Product> findProductByIds(List<UUID> ids);

    Long count();
}
