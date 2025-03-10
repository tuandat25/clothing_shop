package com.tuandat.clothingshop.services.product;

import com.tuandat.clothingshop.dtos.ProductDTO;
import com.tuandat.clothingshop.dtos.ProductImageDTO;
import com.tuandat.clothingshop.exception.DataNotFoundException;
import com.tuandat.clothingshop.models.Product;
import com.tuandat.clothingshop.models.ProductImage;
import com.tuandat.clothingshop.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;

    Product getProductById(Long id) throws Exception;

    Page<ProductResponse> getAllProducts(String keyword,
                                         Long categoryId, PageRequest pageRequest);

    Product updateProduct(long id, ProductDTO dto);

    void deleteProduct(long id);

    boolean existByName(String name);

    ProductImage createProductImage(Product product, ProductImageDTO dto);

    void isValidSize(long productId);

    List<Product> findProductByIds(List<Long> ids);
}
