package com.example.utaDelivery.service;

import com.example.utaDelivery.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long userId);
    User saveUser(User user);
    User updateUser(Long userId, User user);
    void deleteUser(Long userId);
}
