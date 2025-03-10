package com.tuandat.clothingshop.services.order;

import com.tuandat.clothingshop.dtos.OrderDTO;
import com.tuandat.clothingshop.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;
    OrderResponse getOrder(Long id);
    OrderResponse updateOrder(Long id, OrderDTO dto);
    void deleteOrder(Long id);
    List<OrderResponse> getAllOrders(Long userId);
}
