package com.example.utaDelivery.ControllerTest;

import com.example.utaDelivery.DTO.UserDTO;
import com.example.utaDelivery.Entity.User;
import com.example.utaDelivery.exception.ResourceNotFoundException;
import com.example.utaDelivery.controller.UserController;
import com.example.utaDelivery.service.UserService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setUsername("Namaste");
        userDTO.setPassword("turygy");
        userDTO.setEmail("Namaste@example.com");
        userDTO.setPhone("56475857");
        userDTO.setAddress("123 Street, City");

        user = new User();
        user.setUserId(1L);
        user.setUsername("Faizen");
        user.setPassword("utyiuetfd");
        user.setEmail("Faizen@example.com");
        user.setPhone("987568795");
        user.setAddress("123 Street, City");
    }

    @Test
    public void getAllUsersTest() {
        List<User> userList = new ArrayList<>();
        userList.add(user);

        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<UserDTO>> responseEntity = userController.getAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(user.getUserId(), responseEntity.getBody().get(0).getUserId());
    }

    @Test
    public void getUserByIdTest() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<UserDTO> responseEntity = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user.getUserId(), responseEntity.getBody().getUserId());
    }

    @Test
    public void getUserByIdNotFoundTest() {
        when(userService.getUserById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userController.getUserById(2L);
        });

        assertEquals("User not found with id: 2", exception.getMessage());
    }

    @Test
    public void createUserTest() {
        when(userService.saveUser(any(User.class))).thenReturn(user);

        ResponseEntity<UserDTO> responseEntity = userController.createUser(userDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(user.getUserId(), responseEntity.getBody().getUserId());
    }

    @Test
    public void updateUserTest() {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        ResponseEntity<UserDTO> responseEntity = userController.updateUser(1L, userDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user.getUserId(), responseEntity.getBody().getUserId());
    }

    @Test
    public void updateUserNotFoundTest() {
        when(userService.updateUser(eq(2L), any(User.class))).thenThrow(new ResourceNotFoundException("User not found with id: 2"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userController.updateUser(2L, userDTO);
        });

        assertEquals("User not found with id: 2", exception.getMessage());
    }

    @Test
    public void deleteUserTest() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> responseEntity = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void deleteUserNotFoundTest() {
        doThrow(new ResourceNotFoundException("User not found with id: 2")).when(userService).deleteUser(2L);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userController.deleteUser(2L);
        });

        assertEquals("User not found with id: 2", exception.getMessage());
    }
}
