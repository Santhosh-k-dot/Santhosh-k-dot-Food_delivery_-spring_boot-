package com.example.utaDelivery.service;

import com.example.utaDelivery.DTO.OrderItemDTO;

import java.util.List;

public interface OrderItemService {
    OrderItemDTO getOrderItemById(Long orderItemId);
    List<OrderItemDTO> getAllOrderItems();
    OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO);
    OrderItemDTO updateOrderItem(Long orderItemId, OrderItemDTO orderItemDTO);
    void deleteOrderItem(Long orderItemId);
}
