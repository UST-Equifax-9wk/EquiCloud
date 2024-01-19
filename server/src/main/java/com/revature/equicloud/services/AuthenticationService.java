package com.revature.equicloud.services;

import com.revature.equicloud.dtos.AuthenticationRequest;
import com.revature.equicloud.dtos.RegisterRequest;
import com.revature.equicloud.entities.Account;
import com.revature.equicloud.entities.Password;
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


    public String registerUser(RegisterRequest request) {
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
        String token = jwtUtil.generateToken(user);
        return token;
    }

    public String authenticateUser(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getAccountName(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Incorrect username or password");
        }
        UserDetails user = userDetailsService.loadUserByUsername(request.getAccountName());
        String token = jwtUtil.generateToken(user);
        return token;
    }
}
