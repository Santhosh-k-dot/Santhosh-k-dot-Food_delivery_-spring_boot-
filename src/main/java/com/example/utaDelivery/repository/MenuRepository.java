package com.example.utaDelivery.repository;

import com.example.utaDelivery.Entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurant_RestaurantId(Long restaurantId);
}
