package com.urosrelic.spring_jwt.controller;

import com.urosrelic.spring_jwt.dto.LoginRequest;
import com.urosrelic.spring_jwt.dto.RegisterRequest;
import com.urosrelic.spring_jwt.entities.User;
import com.urosrelic.spring_jwt.exception.UsernameExistsException;
import com.urosrelic.spring_jwt.exception.WrongCredentialsException;
import com.urosrelic.spring_jwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    private ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = authService.register(registerRequest);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (UsernameExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    private ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token =  authService.login(loginRequest);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (WrongCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}
