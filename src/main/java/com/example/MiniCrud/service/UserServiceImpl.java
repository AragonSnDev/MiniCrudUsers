package com.example.MiniCrud.service;

import com.example.MiniCrud.entity.User;
import com.example.MiniCrud.entity.UserRole;
import com.example.MiniCrud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            // Error inesperado
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getUsersList() {
        try{
            // Se optione la lista de todos los usuarios
            return new ArrayList<>(userRepository.findAll());
        } catch (Exception e) {
            // Error inesperado
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserById(Long userId) {
        try{
            // Se optione un usuario en espécifico
            return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));
        }
        catch (Exception  e) {
            // Error inesperado
            throw new RuntimeException("Error interno al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public User updateUser(User user, Long userId) {
        try{
            // Trae el User que se va actualizarr de la BD.
            // Si el usuario no existe se lanza un RuntimeException
            User userToUpdate = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));

            // Se pregunta por la propiedades para sincronizar lo que recibio la peticion (user) y lo que se
            // va a guardar en la base de datos (userToUpdate)
            if(propertyisNotEmpty(user.getUserName())){
                userToUpdate.setUserName(user.getUserName());
            }
            if(propertyisNotEmpty(user.getUserEmail())){
                userToUpdate.setUserEmail(user.getUserEmail());
            }
            if(propertyisNotEmpty(user.getRole())){
                userToUpdate.setRole(user.getRole());
            }

            // Se actualiza en DB la informacion de userToUpdate en el User que corresponde con su id.
            return userRepository.save(userToUpdate);

        } catch (RuntimeException e) {
            // Excepcion si no se encuentra el id
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage());
        } catch (Exception  e) {
            // Error inesperado
            throw new RuntimeException("Error interno al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public String deleteUserById(Long userId) {
        try{
            // Se verifica que el usuario exista
            if(!userRepository.existsById(userId)){
                //Se lanza un mensaje de que el usuario no existe
                return "Usuario no encontrado con id: " + userId;
            }

            userRepository.deleteById(userId);
            return "Usuario eliminado exitosamente";

        }  catch (Exception e) {
            // Error inesperado
            throw new RuntimeException("Error interno al eliminar usuario", e);
        }
    }

    //Regresa true si la propiedad tiene un valor
    private boolean propertyisNotEmpty(String value) {
        return value != null && !value.isBlank();
    }

    //Sobrecarga de propertyIsNotEmpty para el enum UserRole
    private boolean propertyisNotEmpty(UserRole value) {
        return value != null;
    }
}
