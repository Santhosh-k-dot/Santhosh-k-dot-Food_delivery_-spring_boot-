package com.example.utaDelivery.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import com.example.utaDelivery.Entity.Restaurant;
import com.example.utaDelivery.exception.DatabaseException;
import com.example.utaDelivery.exception.ResourceNotFoundException;
import com.example.utaDelivery.impl.RestaurantServiceImpl;
import com.example.utaDelivery.repository.RestaurantRepository;

public class RestaurantServiceTest {

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRestaurants() {
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setRestaurantId(1L);
        restaurant1.setName("Restaurant1");

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setRestaurantId(2L);
        restaurant2.setName("Restaurant2");

        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(restaurant1, restaurant2));

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        assertEquals(2, restaurants.size());
        assertEquals("Restaurant1", restaurants.get(0).getName());
        assertEquals("Restaurant2", restaurants.get(1).getName());
    }

    @Test
    public void testGetRestaurantById() {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1L);
        restaurant.setName("Test Restaurant");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        Optional<Restaurant> foundRestaurant = restaurantService.getRestaurantById(1L);
        assertTrue(foundRestaurant.isPresent());
        assertEquals("Test Restaurant", foundRestaurant.get().getName());
    }

    @Test
    public void testGetRestaurantById_NotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.getRestaurantById(1L));
    }

    @Test
    public void testSaveRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurant);
        assertEquals("Test Restaurant", savedRestaurant.getName());
    }

    @Test
    public void testSaveRestaurant_DatabaseException() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");

        when(restaurantRepository.save(any(Restaurant.class))).thenThrow(new DataAccessException("...") {});

        assertThrows(DatabaseException.class, () -> restaurantService.saveRestaurant(restaurant));
    }

    @Test
    public void testUpdateRestaurant() {
        Restaurant existingRestaurant = new Restaurant();
        existingRestaurant.setRestaurantId(1L);
        existingRestaurant.setName("Existing Restaurant");

        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setName("Updated Restaurant");
        updatedRestaurant.setEmail("updated@test.com");
        updatedRestaurant.setPhone("1234567890");
        updatedRestaurant.setAddress("New Address");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(existingRestaurant);

        Restaurant result = restaurantService.updateRestaurant(1L, updatedRestaurant);
        assertEquals("Updated Restaurant", result.getName());
        assertEquals("updated@test.com", result.getEmail());
        assertEquals("1234567890", result.getPhone());
        assertEquals("New Address", result.getAddress());
    }

    @Test
    public void testUpdateRestaurant_NotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1L);
        restaurant.setName("Updated Restaurant");

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.updateRestaurant(1L, restaurant));
    }

    @Test
    public void testDeleteRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1L);
        restaurant.setName("Test Restaurant");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        doNothing().when(restaurantRepository).delete(any(Restaurant.class));

        restaurantService.deleteRestaurant(1L);

        verify(restaurantRepository, times(1)).delete(restaurant);
    }

    @Test
    public void testDeleteRestaurant_NotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.deleteRestaurant(1L));
    }
}
