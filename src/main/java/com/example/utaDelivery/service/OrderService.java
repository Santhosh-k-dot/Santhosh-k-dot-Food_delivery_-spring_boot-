package com.example.utaDelivery.service;

import com.example.utaDelivery.DTO.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO getOrderById(Long orderId);
    List<OrderDTO> getAllOrders();
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO updateOrder(Long orderId, OrderDTO orderDTO);
    void deleteOrder(Long orderId);
}
