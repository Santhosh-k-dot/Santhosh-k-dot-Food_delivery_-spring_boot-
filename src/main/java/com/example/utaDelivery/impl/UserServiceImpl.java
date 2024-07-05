package com.example.utaDelivery.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.utaDelivery.Entity.User;
import com.example.utaDelivery.exception.ResourceNotFoundException;
import com.example.utaDelivery.repository.UserRepository;
import com.example.utaDelivery.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User saveUser(User user) {
        // Here you should hash the password before saving
        user.setPassword(hashPassword(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, User user) {
        Optional<User> existingUserOptional = userRepository.findById(userId);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setAddress(user.getAddress());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(hashPassword(user.getPassword())); // Hash the password before updating
            }
            return userRepository.save(existingUser);
        } else {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
        } else {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }

    private String hashPassword(String password) {
        // Implement password hashing here
        // Example using BCrypt:
        // return new BCryptPasswordEncoder().encode(password);
        return password; // This should be replaced with the actual hashing logic
    }
}
