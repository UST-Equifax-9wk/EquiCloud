package com.revature.equicloud.controllers;


import com.revature.equicloud.entities.Account;
import com.revature.equicloud.exceptions.InvalidInputException;
import com.revature.equicloud.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AccountController {
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService=accountService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Testing authentication");
    }
}
