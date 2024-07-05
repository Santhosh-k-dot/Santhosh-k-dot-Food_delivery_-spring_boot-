package com.example.utaDelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.utaDelivery.DTO.RestaurantDTO;
import com.example.utaDelivery.Entity.Restaurant;
import com.example.utaDelivery.exception.ResourceNotFoundException;
import com.example.utaDelivery.service.RestaurantService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        List<RestaurantDTO> restaurantDTOs = convertToDTOList(restaurants);
        return new ResponseEntity<>(restaurantDTOs, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long restaurantId) {
        Optional<Restaurant> restaurant = restaurantService.getRestaurantById(restaurantId);
        if (restaurant.isPresent()) {
            return ResponseEntity.ok().body(convertToDTO(restaurant.get()));
        } else {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        Restaurant restaurant = convertToEntity(restaurantDTO);
        Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedRestaurant));
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long restaurantId, @RequestBody RestaurantDTO restaurantDTO) {
        try {
            Restaurant updatedRestaurant = restaurantService.updateRestaurant(restaurantId, convertToEntity(restaurantDTO));
            return ResponseEntity.ok().body(convertToDTO(updatedRestaurant));
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId) {
        try {
            restaurantService.deleteRestaurant(restaurantId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
    }

    // Convert Entity to DTO
    private RestaurantDTO convertToDTO(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setRestaurantId(restaurant.getRestaurantId());
        restaurantDTO.setName(restaurant.getName());
        restaurantDTO.setEmail(restaurant.getEmail());
        restaurantDTO.setPhone(restaurant.getPhone());
        restaurantDTO.setAddress(restaurant.getAddress());
        return restaurantDTO;
    }

    // Convert DTO to Entity
    private Restaurant convertToEntity(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantDTO.getRestaurantId());
        restaurant.setName(restaurantDTO.getName());
        restaurant.setEmail(restaurantDTO.getEmail());
        restaurant.setPhone(restaurantDTO.getPhone());
        restaurant.setAddress(restaurantDTO.getAddress());
        return restaurant;
    }

    // Convert List of Entities to List of DTOs
    private List<RestaurantDTO> convertToDTOList(List<Restaurant> restaurants) {
        return restaurants.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
