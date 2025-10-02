package com.example.MiniCrud.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("Usuario no encontrado con id: " + userId);
    }
}
