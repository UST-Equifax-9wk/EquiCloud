package com.revature.equicloud.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "passwords")
public class Password {

    @Id
    @Column(name = "account_name", unique = true)
    private String accountName;

    @Column(nullable = false)
    private String password;
    public Password() {
    }

    public Password(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
