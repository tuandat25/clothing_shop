package com.tuandat.clothingshop.services.order;

import com.tuandat.clothingshop.dtos.OrderDTO;
import com.tuandat.clothingshop.responses.OrderResponse;

import java.util.*;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;
    OrderResponse getOrder(UUID id);
    OrderResponse updateOrder(UUID id, OrderDTO dto);
    void deleteOrder(UUID id);
    List<OrderResponse> getAllOrders(UUID userId);
    Long count();
}
