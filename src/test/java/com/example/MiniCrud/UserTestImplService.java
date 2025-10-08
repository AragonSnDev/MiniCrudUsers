package com.example.MiniCrud;


import com.example.MiniCrud.entity.User;
import com.example.MiniCrud.entity.UserRole;
import com.example.MiniCrud.repository.UserRepository;
import com.example.MiniCrud.service.UserServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserTestImplService {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User(UserRole.USER,"daniel@email.com","Daniel Aragon",1L);
        // MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser() {
        //Se hace la llamada que se quiere testear
        when(userRepository.save(any())).thenReturn(testUser);

        User newUser = userService.createUser(testUser);

        assertNotNull(newUser);
        assertEquals("Daniel Aragon", newUser.getUserName());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void testGetAllUsers(){
        //Traer todos los usuarios
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        List<User> users = userService.getUsersList();

        //Comprobar que la lista de usuarios no sea null
        assertNotNull(users);
        // Asegurarse de que el metodo findAll() fue llamado solo una vez.
        verify(userRepository,times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        when(userRepository.save(any())).thenReturn(testUser);

        User updatedUser = userService.updateUser(testUser, testUser.getUserId());

        assertNotNull(updatedUser);
        assertEquals("Daniel Aragon",updatedUser.getUserName());

        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(testUser.getUserId());
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        boolean isDeleted = userService.deleteUserById(1L);

        assertTrue(isDeleted);
        verify(userRepository,times(1)).deleteById(testUser.getUserId());
    }
}
