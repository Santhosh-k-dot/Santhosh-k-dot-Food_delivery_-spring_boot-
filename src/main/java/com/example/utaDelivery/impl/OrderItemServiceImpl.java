package com.example.utaDelivery.impl;


import com.example.utaDelivery.DTO.OrderItemDTO;
import com.example.utaDelivery.Entity.Order;
import com.example.utaDelivery.Entity.OrderItem;
import com.example.utaDelivery.Entity.Menu;
import com.example.utaDelivery.exception.ResourceNotFoundException;
import com.example.utaDelivery.repository.OrderRepository;
import com.example.utaDelivery.repository.OrderItemRepository;
import com.example.utaDelivery.repository.MenuRepository;
import com.example.utaDelivery.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public OrderItemDTO getOrderItemById(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));
        return convertToDTO(orderItem);
    }

    @Override
    public List<OrderItemDTO> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        return orderItems.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO) {
        Order order = orderRepository.findById(orderItemDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        Menu menu = menuRepository.findById(orderItemDTO.getMenuId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
        OrderItem orderItem = convertToEntity(orderItemDTO, order, menu);
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return convertToDTO(savedOrderItem);
    }

    @Override
    public OrderItemDTO updateOrderItem(Long orderItemId, OrderItemDTO orderItemDTO) {
        OrderItem existingOrderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));
        Order order = orderRepository.findById(orderItemDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        Menu menu = menuRepository.findById(orderItemDTO.getMenuId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));

        existingOrderItem.setOrder(order);
        existingOrderItem.setMenu(menu);
        existingOrderItem.setQuantity(orderItemDTO.getQuantity());

        OrderItem updatedOrderItem = orderItemRepository.save(existingOrderItem);
        return convertToDTO(updatedOrderItem);
    }

    @Override
    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));
        orderItemRepository.delete(orderItem);
    }

    private OrderItemDTO convertToDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setOrderItemId(orderItem.getOrderItemId());
        orderItemDTO.setOrderId(orderItem.getOrder().getOrderId());
        orderItemDTO.setMenuId(orderItem.getMenu().getMenuId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        return orderItemDTO;
    }

    private OrderItem convertToEntity(OrderItemDTO orderItemDTO, Order order, Menu menu) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(orderItemDTO.getOrderItemId());
        orderItem.setOrder(order);
        orderItem.setMenu(menu);
        orderItem.setQuantity(orderItemDTO.getQuantity());
        return orderItem;
    }
}
