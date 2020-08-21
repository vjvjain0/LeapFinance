package com.leapfinance.controllers;

import com.leapfinance.exceptions.BadCredentialsException;
import com.leapfinance.models.User;
import com.leapfinance.responses.UserResponse;
import com.leapfinance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class UserController {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity handle(Exception ex) {
//        return new ResponseEntity(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
//    }

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid User newUser) {
        return UserResponse.from(userService.save(newUser));
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody @Valid User user) {
        return UserResponse.from(userService.login(user));
    }
}