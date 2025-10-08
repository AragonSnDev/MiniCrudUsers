package com.example.MiniCrud.service;

import com.example.MiniCrud.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User createUser(User user);
    List<User> getUsersList();
    User getUserById(Long userId);
    User updateUser(User user, Long userId);
    boolean deleteUserById(Long userId);
}
