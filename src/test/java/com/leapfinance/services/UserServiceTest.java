package com.leapfinance.services;

import com.leapfinance.repositories.UserRepository;
import com.leapfinance.exceptions.BadCredentialsException;
import com.leapfinance.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void when_password_is_not_valid_throw_BadCredentialsException() {
        User u = new User("user", "password");
        assertThrows(BadCredentialsException.class, () -> userService.save(u));
    }

    @Test
    void when_username_exist_throw_BadCredentialsException() {
        when(userRepository.countByUsername(anyString())).thenReturn(1L);
        User u = new User("user", "password123");
        String msg = assertThrows(BadCredentialsException.class, () -> userService.save(u)).getMessage();
        assertEquals(msg, "username is already taken");
    }

    @Test
    void when_username_does_not_exist_throw_BadCredentialsException() {
        when(userRepository.countByUsername(anyString())).thenReturn(1L);
        User u1 = new User("user1", "password123");
        assertThrows(BadCredentialsException.class, () -> userService.login(u1));
    }
}