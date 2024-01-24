package com.revature.equicloud.services;

import com.revature.equicloud.dtos.AuthenticationRequest;

import com.revature.equicloud.dtos.RegisterRequest;
import com.revature.equicloud.entities.Account;
import com.revature.equicloud.entities.Password;
import com.revature.equicloud.exceptions.EmailAlreadyExistsException;
import com.revature.equicloud.exceptions.UserAlreadyExistsException;
import com.revature.equicloud.repositories.AccountRepository;
import com.revature.equicloud.repositories.PasswordRepository;
import com.revature.equicloud.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

@Service
public class AuthenticationService {

    private PasswordRepository passwordRepository;

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationService (PasswordRepository userRepository,
                             AccountRepository accountRepository,
                             PasswordEncoder passwordEncoder,
                             JwtUtil jwtUtil,
                             AuthenticationManager authenticationManager,
                                   UserDetailsService userDetailsService){
        this.passwordRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }


    /**
     * Registers a new user account in the system. The method creates an account and password record,
     * saves them to the repository, and generates a JWT token for the user.
     *
     * @param request The registration request containing the user's account information.
     * @return A JWT token generated for the newly registered user.
     */
    public void registerUser(RegisterRequest request) {
        passwordRepository.findByAccountName(request.getAccountName())
                .ifPresent(existingUser -> {
                    throw new UserAlreadyExistsException("User already exists with username: " + request.getAccountName());
                });

        accountRepository.findByEmail(request.getEmail())
                .ifPresent(existingUser -> {
                    throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
                });
        Account account = new Account(request.getAccountName(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail());
        accountRepository.save(account);
        Password password = new Password(request.getAccountName(), passwordEncoder.encode(request.getPassword()));
        passwordRepository.save(password);
        UserDetails user = User.withUsername(password.getAccountName())
                .password(password.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }

    /**
     * Authenticates a user based on the provided credentials in the authentication request.
     * If authentication is successful, it generates and returns a JWT token for the user.
     *
     * @param request The authentication request containing the user's account name and password.
     * @return A JWT token generated for the authenticated user.
     * @throws BadCredentialsException if the provided credentials are incorrect.
     */
    public String authenticateUser(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getAccountName(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }
        UserDetails user = userDetailsService.loadUserByUsername(request.getAccountName());
        String token = jwtUtil.generateToken(user);
        return token;
    }
}
