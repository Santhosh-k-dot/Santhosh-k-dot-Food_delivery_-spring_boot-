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

import com.example.utaDelivery.Entity.User;
import com.example.utaDelivery.exception.ResourceNotFoundException;
import com.example.utaDelivery.impl.UserServiceImpl;
import com.example.utaDelivery.repository.UserRepository;

public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setUserId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUserId(2L);
        user2.setUsername("user2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);
        assertEquals("password", savedUser.getPassword()); // Assuming hashPassword is not actually hashing for this test
    }

    @Test
    public void testUpdateUser() {
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("existinguser");
        existingUser.setPassword("oldpassword");

        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("updatedemail@test.com");
        updatedUser.setPhone("1234567890");
        updatedUser.setAddress("New Address");
        updatedUser.setPassword("newpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.updateUser(1L, updatedUser);
        assertEquals("updateduser", result.getUsername());
        assertEquals("updatedemail@test.com", result.getEmail());
        assertEquals("1234567890", result.getPhone());
        assertEquals("New Address", result.getAddress());
        assertEquals("newpassword", result.getPassword()); // Assuming hashPassword is not actually hashing for this test
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User user = new User();
        user.setUserId(1L);
        user.setUsername("updateduser");

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, user));
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
    }
}
