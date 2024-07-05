package com.example.utaDelivery.impl;

import com.example.utaDelivery.Entity.Restaurant;
import com.example.utaDelivery.exception.DatabaseException;
import com.example.utaDelivery.exception.ResourceNotFoundException;
import com.example.utaDelivery.repository.RestaurantRepository;
import com.example.utaDelivery.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Optional<Restaurant> getRestaurantById(Long restaurantId) throws ResourceNotFoundException {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant.isEmpty()) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
        return restaurant;
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        try {
            return restaurantRepository.save(restaurant);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to save restaurant");
        }
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, Restaurant restaurantDetails) throws ResourceNotFoundException {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            restaurant.setName(restaurantDetails.getName());
            restaurant.setEmail(restaurantDetails.getEmail());
            restaurant.setPhone(restaurantDetails.getPhone());
            restaurant.setAddress(restaurantDetails.getAddress());
            return restaurantRepository.save(restaurant);
        } else {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws ResourceNotFoundException {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isPresent()) {
            restaurantRepository.delete(optionalRestaurant.get());
        } else {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
    }
}
