package com.revature.revportal.controllers;

import com.revature.revportal.entities.Account;
import com.revature.revportal.entities.Password;
import com.revature.revportal.services.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private AccountService accountService;

    @Autowired
    AuthController(AuthenticationManager authenticationManager, AccountService accountService){
        this.authenticationManager=authenticationManager;
        this.accountService=accountService;
    }
    @PostMapping("/login")
    public ResponseEntity<Account> customLogin(@RequestBody Password password) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(password.getAccountName(), password.getPassword())
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        Account account = accountService.findAccountByName(password.getAccountName());
//        if(authentication.getName()!=null){
//            return new ResponseEntity<>(account, HttpStatus.ACCEPTED);
//        }
//        else{
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }

        return null;
    }


    //Exception
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAuthFail(EntityNotFoundException ex) {
        return ex.getMessage();
    }

}