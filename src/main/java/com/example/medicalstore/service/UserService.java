package com.example.medicalstore.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.medicalstore.entity.User;
import com.example.medicalstore.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User login(String email, String password) {

        Optional<User> optionalUser =
                userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            if (user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }
}