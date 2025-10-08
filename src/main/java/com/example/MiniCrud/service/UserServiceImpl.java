package com.example.MiniCrud.service;

import com.example.MiniCrud.entity.User;
import com.example.MiniCrud.entity.UserRole;
import com.example.MiniCrud.exceptions.UserNotFoundException;
import com.example.MiniCrud.exceptions.UserOperationException;
import com.example.MiniCrud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {

        try{
            // Se guarda un User nuevo
            return userRepository.save((user));

        } catch (Exception e) {
            throw new UserOperationException("Error al obtener la lista de usuarios", e);
        }
    }

    @Override
    public List<User> getUsersList() {
        try{
            return new ArrayList<>(userRepository.findAll());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserById(Long userId) {
        try{
            return getUserOrThrow(userId);
        }
        catch (Exception  e) {
            // Error inesperado
            throw new RuntimeException("Error interno al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public User updateUser(User updatedUser, Long userId) {
        return userRepository.findById(userId).map(user -> userRepository.save(user)).orElse(null);
    }

    @Override
    public boolean deleteUserById(Long userId) {
       return userRepository.findById(userId).map(user -> {
           userRepository.deleteById(user.getUserId());
           return true;
       }).orElse(false);
    }

    private boolean propertyIsNotEmpty(String value) {
        return value != null && !value.isBlank();
    }

    //Sobrecarga de propertyIsNotEmpty para el enum UserRole
    private boolean propertyIsNotEmpty(UserRole value) {

        return value != null;
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void updateUserFields(User existingUser, User updatedUserData) {

        if (propertyIsNotEmpty(updatedUserData.getUserName())) {
            existingUser.setUserName(updatedUserData.getUserName());
        }
        if (propertyIsNotEmpty(updatedUserData.getUserEmail())) {
            existingUser.setUserEmail(updatedUserData.getUserEmail());
        }
        if (propertyIsNotEmpty(updatedUserData.getRole())) {
            existingUser.setRole(updatedUserData.getRole());
        }
    }




}
