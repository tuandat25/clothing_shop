package com.tuandat.clothingshop.services.order_detail;

import com.tuandat.clothingshop.dtos.OrderDetailDTO;
import com.tuandat.clothingshop.models.OrderDetail;

import java.util.*;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO dto) throws Exception;
    OrderDetail getOrderDetail(UUID id);
    OrderDetail updateOrderDetail(UUID id, OrderDetailDTO orderDetailDTO);
    void deleteOrderDetail(UUID id);
    List<OrderDetail> getOrderDetails(UUID orderId);
}
