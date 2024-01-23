package com.revature.equicloud.controllers;

import com.revature.equicloud.dtos.AuthenticationRequest;
import com.revature.equicloud.dtos.AuthenticationResponse;
import com.revature.equicloud.dtos.RegisterRequest;
import com.revature.equicloud.services.AccountService;
import com.revature.equicloud.services.AuthenticationService;
import com.revature.equicloud.services.UserDetailsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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


    private final AuthenticationService authenticationService;
    private final AccountService accountService;

    @Autowired
    AuthController(AccountService accountService,
                   AuthenticationService authenticationService){
        this.accountService=accountService;
        this.authenticationService=authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request){
        String token = authenticationService.registerUser(request);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request,
                                                               HttpServletResponse response) {
        String token = authenticationService.authenticateUser(request);

        AuthenticationResponse authResponse = accountService.findAccount(request.getAccountName());
        Cookie jwtCookie = new Cookie("jwtToken", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(true);

        response.addCookie(jwtCookie);

//        AuthenticationResponse authResponse = new AuthenticationResponse(token);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){

        Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setSecure(true);
        jwtCookie.setHttpOnly(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

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


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleIncorrectUsernamePassword(IllegalArgumentException ex) { return ex.getMessage(); }

}