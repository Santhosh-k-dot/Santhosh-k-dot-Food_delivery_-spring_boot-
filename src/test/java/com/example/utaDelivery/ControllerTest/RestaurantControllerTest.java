package com.example.utaDelivery.ControllerTest;

import com.example.utaDelivery.DTO.RestaurantDTO;
import com.example.utaDelivery.Entity.Restaurant;
import com.example.utaDelivery.controller.RestaurantController;
import com.example.utaDelivery.exception.ResourceNotFoundException;
import com.example.utaDelivery.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantControllerTest {

    @InjectMocks
    private RestaurantController restaurantController;

    @Mock
    private RestaurantService restaurantService;

    private RestaurantDTO restaurantDTO;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurantDTO = new RestaurantDTO();
        restaurantDTO.setRestaurantId(1L);
        restaurantDTO.setName("Test Restaurant");
        restaurantDTO.setEmail("test.restaurant@example.com");
        restaurantDTO.setPhone("1234567890");
        restaurantDTO.setAddress("456 Street, City");

        restaurant = new Restaurant();
        restaurant.setRestaurantId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setEmail("test.restaurant@example.com");
        restaurant.setPhone("1234567890");
        restaurant.setAddress("456 Street, City");
    }

    @Test
    public void testGetAllRestaurants() {
        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(restaurant);

        when(restaurantService.getAllRestaurants()).thenReturn(restaurantList);

        ResponseEntity<List<RestaurantDTO>> responseEntity = restaurantController.getAllRestaurants();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(restaurant.getRestaurantId(), responseEntity.getBody().get(0).getRestaurantId());
    }

    @Test
    public void testGetRestaurantById() {
        when(restaurantService.getRestaurantById(1L)).thenReturn(Optional.of(restaurant));

        ResponseEntity<RestaurantDTO> responseEntity = restaurantController.getRestaurantById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(restaurant.getRestaurantId(), responseEntity.getBody().getRestaurantId());
    }

    @Test
    public void testGetRestaurantByIdNotFound() {
        when(restaurantService.getRestaurantById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restaurantController.getRestaurantById(2L);
        });

        assertEquals("Restaurant not found with id: 2", exception.getMessage());
    }

    @Test
    public void testCreateRestaurant() {
        when(restaurantService.saveRestaurant(any(Restaurant.class))).thenReturn(restaurant);

        ResponseEntity<RestaurantDTO> responseEntity = restaurantController.createRestaurant(restaurantDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(restaurant.getRestaurantId(), responseEntity.getBody().getRestaurantId());
    }

    @Test
    public void testUpdateRestaurant() {
        when(restaurantService.updateRestaurant(eq(1L), any(Restaurant.class))).thenReturn(restaurant);

        ResponseEntity<RestaurantDTO> responseEntity = restaurantController.updateRestaurant(1L, restaurantDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(restaurant.getRestaurantId(), responseEntity.getBody().getRestaurantId());
    }

    @Test
    public void testUpdateRestaurantNotFound() {
        when(restaurantService.updateRestaurant(eq(2L), any(Restaurant.class)))
                .thenThrow(new ResourceNotFoundException("Restaurant not found with id: 2"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restaurantController.updateRestaurant(2L, restaurantDTO);
        });

        assertEquals("Restaurant not found with id: 2", exception.getMessage());
    }

    @Test
    public void testDeleteRestaurant() {
        doNothing().when(restaurantService).deleteRestaurant(1L);

        ResponseEntity<Void> responseEntity = restaurantController.deleteRestaurant(1L);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteRestaurantNotFound() {
        doThrow(new ResourceNotFoundException("Restaurant not found with id: 2")).when(restaurantService).deleteRestaurant(2L);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restaurantController.deleteRestaurant(2L);
        });

        assertEquals("Restaurant not found with id: 2", exception.getMessage());
    }
}
