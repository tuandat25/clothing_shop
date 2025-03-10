package com.tuandat.clothingshop.services.order_detail;

import com.tuandat.clothingshop.dtos.OrderDetailDTO;
import com.tuandat.clothingshop.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO dto) throws Exception;
    OrderDetail getOrderDetail(Long id);
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO);
    void deleteOrderDetail(Long id);
    List<OrderDetail> getOrderDetails(Long orderId);
}
