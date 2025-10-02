package com.example.MiniCrud.controller;

import com.example.MiniCrud.entity.User;
import com.example.MiniCrud.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired private UserService userService;

    @PostMapping("/users") public ResponseEntity<?> saveUser(@RequestBody @Valid User user, BindingResult result){

        if(result.hasErrors()){ return createResonseWithError(result);}
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
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
    public User updateUser(@RequestBody User user, @PathVariable("id") Long userId){
        return userService.updateUser(user,userId);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUserById(@PathVariable("id") Long userId){

        return userService.deleteUserById(userId);
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
