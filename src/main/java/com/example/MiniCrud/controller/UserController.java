package com.example.MiniCrud.controller;

import com.example.MiniCrud.entity.User;
import com.example.MiniCrud.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> saveUser(@RequestBody @Valid User user, BindingResult result){

        if(result.hasErrors()){ return createResonseWithError(result);}
        User savedUser = userService.createUser(user);
        return createReponseOkObject("Usuario creado exitosamente",savedUser);
    }

    @GetMapping("/users")
    public List<User> fetchUsersList(){
        return userService.getUsersList();
    }

    @GetMapping("/users/{id}")
    public User fetchUserById(@PathVariable("id") Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable("id") Long userId){
        User updatedUser = userService.updateUser(user,userId);
        if(updatedUser == null){
            return createReponseStatusNoObject("Usuario no encontrado",HttpStatus.NOT_FOUND);
        }
        return createReponseOkObject("Usuario creado exitosamente",updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long userId){
        boolean deleted = userService.deleteUserById(userId);
        if(!deleted){
            return createReponseStatusNoObject("Usuario no encontrado",HttpStatus.NOT_FOUND);
        }
        return createReponseOkNoObject("Usuario eliminado existosamente");
    }

    private ResponseEntity<?> createReponseOkObject(String message,User user){
        return ResponseEntity.ok(Map.of("message",message,"data",user));
    }

    private ResponseEntity<?> createReponseOkNoObject(String message){
        return ResponseEntity.ok(Map.of("message",message));
    }

    private ResponseEntity<?> createReponseStatusNoObject(String message, HttpStatus status){
        return ResponseEntity.status(status).body(Map.of("message",message));
    }

    private ResponseEntity<?> createResonseWithError(BindingResult result){
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        // Json con mensajes de error
        return ResponseEntity.badRequest().body(errors);
    }
}
