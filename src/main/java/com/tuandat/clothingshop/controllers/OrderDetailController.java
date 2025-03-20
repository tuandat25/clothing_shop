package com.tuandat.clothingshop.controllers;

import com.tuandat.clothingshop.dtos.OrderDetailDTO;
import com.tuandat.clothingshop.models.OrderDetail;
import com.tuandat.clothingshop.responses.OrderDetailResponse;
import com.tuandat.clothingshop.services.order_detail.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO
            , BindingResult bindingResult){
        try{
            if (bindingResult.hasErrors()){
                List<String> errorsMessage = bindingResult.getFieldErrors()
                        .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorsMessage);
            }
            OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("id") UUID id){
        try{
            OrderDetail orderDetail= orderDetailService.getOrderDetail(id);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable("orderId") UUID id){
        try{
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetails(id);
            List<OrderDetailResponse> orderDetailResponses= orderDetails.stream().map(OrderDetailResponse::fromOrderDetail).toList();
            return ResponseEntity.ok().body(orderDetailResponses);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") UUID id,
                                               @Valid @RequestBody OrderDetailDTO orderDetailDTO,
                                               BindingResult bindingResult){
        try {
            if (bindingResult.hasErrors()){
                List<String> errorsMessage = bindingResult.getFieldErrors()
                        .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorsMessage);
            }
            OrderDetail orderDetail= orderDetailService.updateOrderDetail(id,orderDetailDTO);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") UUID id){
        try {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
