package com.example.utaDelivery.impl;

import com.example.utaDelivery.DTO.OrderDTO;
import com.example.utaDelivery.Entity.Order;
import com.example.utaDelivery.Entity.User;
import com.example.utaDelivery.Entity.Restaurant;
import com.example.utaDelivery.exception.ResourceNotFoundException;
import com.example.utaDelivery.repository.OrderRepository;
import com.example.utaDelivery.repository.UserRepository;
import com.example.utaDelivery.repository.RestaurantRepository;
import com.example.utaDelivery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return convertToDTO(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Restaurant restaurant = restaurantRepository.findById(orderDTO.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        Order order = convertToEntity(orderDTO, user, restaurant);
        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Restaurant restaurant = restaurantRepository.findById(orderDTO.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        
        existingOrder.setUser(user);
        existingOrder.setRestaurant(restaurant);
        existingOrder.setOrderDate(orderDTO.getOrderDate());
        existingOrder.setStatus(orderDTO.getStatus());
        
        Order updatedOrder = orderRepository.save(existingOrder);
        return convertToDTO(updatedOrder);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setUserId(order.getUser().getUserId());
        orderDTO.setRestaurantId(order.getRestaurant().getRestaurantId());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setStatus(order.getStatus());
        return orderDTO;
    }

    private Order convertToEntity(OrderDTO orderDTO, User user, Restaurant restaurant) {
        Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setOrderDate(orderDTO.getOrderDate());
        order.setStatus(orderDTO.getStatus());
        return order;
    }
}
