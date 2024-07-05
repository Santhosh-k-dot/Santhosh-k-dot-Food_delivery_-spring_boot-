package com.example.utaDelivery.controller;


import com.example.utaDelivery.DTO.OrderItemDTO;
import com.example.utaDelivery.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/{orderItemId}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Long orderItemId) {
        OrderItemDTO orderItemDTO = orderItemService.getOrderItemById(orderItemId);
        return ResponseEntity.ok(orderItemDTO);
    }

    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems() {
        List<OrderItemDTO> orderItemDTOs = orderItemService.getAllOrderItems();
        return ResponseEntity.ok(orderItemDTOs);
    }

    @PostMapping
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        OrderItemDTO createdOrderItem = orderItemService.createOrderItem(orderItemDTO);
        return ResponseEntity.status(201).body(createdOrderItem);
    }

    @PutMapping("/{orderItemId}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(@PathVariable Long orderItemId, @RequestBody OrderItemDTO orderItemDTO) {
        OrderItemDTO updatedOrderItem = orderItemService.updateOrderItem(orderItemId, orderItemDTO);
        return ResponseEntity.ok(updatedOrderItem);
    }

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.noContent().build();
    }
}
