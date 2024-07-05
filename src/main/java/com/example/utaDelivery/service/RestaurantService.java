package com.example.utaDelivery.service;

import com.example.utaDelivery.Entity.Restaurant;
import com.example.utaDelivery.exception.DataValidationException;
import com.example.utaDelivery.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    List<Restaurant> getAllRestaurants();
    Optional<Restaurant> getRestaurantById(Long restaurantId) throws ResourceNotFoundException;
    Restaurant saveRestaurant(Restaurant restaurant) throws DataValidationException;
    Restaurant updateRestaurant(Long restaurantId, Restaurant restaurantDetails) throws ResourceNotFoundException, DataValidationException;
    void deleteRestaurant(Long restaurantId) throws ResourceNotFoundException;
}
