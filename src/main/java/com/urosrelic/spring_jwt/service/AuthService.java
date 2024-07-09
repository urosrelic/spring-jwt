package com.urosrelic.spring_jwt.service;

import com.urosrelic.spring_jwt.dto.LoginRequest;
import com.urosrelic.spring_jwt.dto.RegisterRequest;
import com.urosrelic.spring_jwt.entities.User;
import com.urosrelic.spring_jwt.enums.Role;
import com.urosrelic.spring_jwt.exception.UsernameExistsException;
import com.urosrelic.spring_jwt.exception.WrongCredentialsException;
import com.urosrelic.spring_jwt.repositories.UserRepository;
import com.urosrelic.spring_jwt.security.JwtService;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public User register(RegisterRequest registerRequest) throws UsernameExistsException {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UsernameExistsException("Username is already taken");
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public String login(LoginRequest loginRequest) throws WrongCredentialsException {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new WrongCredentialsException("User doesn't exist"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new WrongCredentialsException("Password incorrect");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        return jwtService.generateToken(user);
    }

}
