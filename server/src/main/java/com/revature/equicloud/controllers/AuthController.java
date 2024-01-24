package com.revature.equicloud.controllers;

import com.revature.equicloud.dtos.AuthenticationRequest;
import com.revature.equicloud.dtos.AuthenticationResponse;
import com.revature.equicloud.dtos.RegisterRequest;
import com.revature.equicloud.exceptions.EmailAlreadyExistsException;
import com.revature.equicloud.exceptions.UserAlreadyExistsException;
import com.revature.equicloud.services.AccountService;
import com.revature.equicloud.services.AuthenticationService;
import com.revature.equicloud.services.UserDetailsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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

    /**
     * Handles the user registration process.
     * It calls a service to register the user.
     *
     * @param request The registration request containing the user's details.
     * @return A ResponseEntity with HTTP status OK if the registration is successful.
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request){
        authenticationService.registerUser(request);
        return ResponseEntity.ok().build();
    }


    /**
     * Authenticates a user. If successful, it generates a JWT token,
     * creates a cookie with this token, and returns the user's account details together with the JWT cookie.
     *
     * @param request The authentication request containing the user's credentials.
     * @param response The HttpServletResponse in which the JWT cookie is set.
     * @return A ResponseEntity containing the authenticated user's details.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request,
                                                               HttpServletResponse response) {
        String token = authenticationService.authenticateUser(request);

        AuthenticationResponse authResponse = accountService.findAccount(request.getAccountName());
        Cookie jwtCookie = new Cookie("jwtToken", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(true);

        response.addCookie(jwtCookie);

        return ResponseEntity.ok(authResponse);
    }


    /**
     * Handles the logout process by invalidating the JWT cookie.
     * @param response The HttpServletResponse in which the JWT cookie is invalidated.
     */
    @GetMapping("/logout")
    public void logout(HttpServletResponse response){

        Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setSecure(true);
        jwtCookie.setHttpOnly(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

    }


    /** Exception Handlers
     */


    /**
     * Handles EntityNotFoundExceptions thrown during authentication.
     *
     * @param ex The exception that was thrown.
     * @return A message indicating the account was not found for authenticated user
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAccountNotFound(EntityNotFoundException ex) {
        return ex.getMessage();
    }

    /**
     * Handles BadCredentialsExceptions thrown during authentication.
     *
     * @param ex The exception that was thrown.
     * @return A message describing the invalid credentials.
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleBadCred(BadCredentialsException ex) { return ex.getMessage(); }


    /**
     * Handles UserAlreadyExistsExceptions during user registration.
     *
     * @param ex The exception that was thrown.
     * @return A message indicating that the user already exists.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserAlreadyExist(UserAlreadyExistsException ex) { return ex.getMessage(); }


    /**
     * Handles EmailAlreadyExistsExceptions during user registration.
     *
     * @param ex The exception that was thrown.
     * @return A message indicating that the email is already in use.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmailAlreadyExist(EmailAlreadyExistsException ex) { return ex.getMessage(); }
}