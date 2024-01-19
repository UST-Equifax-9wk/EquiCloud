package com.revature.equicloud.controllers;

import com.revature.equicloud.dtos.AuthenticationRequest;
import com.revature.equicloud.dtos.RegisterRequest;
import com.revature.equicloud.services.AccountService;
import com.revature.equicloud.services.AuthenticationService;
import com.revature.equicloud.services.UserDetailsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {


    private AuthenticationService authenticationService;
    private AccountService accountService;

    @Autowired
    AuthController(AccountService accountService,
                   AuthenticationService authenticationService){
        this.accountService=accountService;
        this.authenticationService=authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
        String token = authenticationService.registerUser(request);
        return ResponseEntity.ok(token);
    }
    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        String token = authenticationService.authenticateUser(request);
        return ResponseEntity.ok(token);
    }


    //Exception
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAuthFail(EntityNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleBadCred(BadCredentialsException ex) { return ex.getMessage(); }

}