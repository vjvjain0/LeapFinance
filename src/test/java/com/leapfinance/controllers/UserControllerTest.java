package com.leapfinance.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leapfinance.exceptions.BadCredentialsException;
import com.leapfinance.models.User;
import com.leapfinance.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    void when_username_is_empty_then_return_400() throws Exception {
        User u = new User("", "password");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u));
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        requestBuilder = MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u));
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void when_password_is_empty_then_return_400() throws Exception {
        User u = new User("user", "");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u));
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        requestBuilder = MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u));
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void when_valid_input_in_register_then_return_2xx() throws Exception {
        when(userService.save(any())).thenReturn(new User());
        User u = new User("user", "password123");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u));
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful());

        verify(userService, times(1)).save(any());
    }

    @Test
    void when_valid_input_in_login_then_return_2xx() throws Exception {
        when(userService.login(any())).thenReturn(new User());
        User u = new User("user", "password123");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u));
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful());

        verify(userService, times(1)).login(any());
    }

    @Test
    void when_service_throws_BadCredentialException_then_return_400() throws Exception {
        when(userService.save(any())).thenThrow(BadCredentialsException.class);
        User u = new User("user", "password123");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u));
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError());

        verify(userService, times(1)).save(any());

        when(userService.login(any())).thenThrow(BadCredentialsException.class);
        requestBuilder = MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u));
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError());

        verify(userService, times(1)).login(any());
    }
}