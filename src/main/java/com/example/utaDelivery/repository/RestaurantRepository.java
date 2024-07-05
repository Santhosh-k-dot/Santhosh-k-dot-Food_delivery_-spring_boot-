package com.example.utaDelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.utaDelivery.Entity.Restaurant;



@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // Additional query methods can be defined here if needed
}