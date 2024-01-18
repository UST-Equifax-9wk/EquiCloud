package com.revature.equicloud.services;

import com.revature.equicloud.entities.Account;
import com.revature.equicloud.entities.Password;
import com.revature.equicloud.repositories.AccountRepository;
import com.revature.equicloud.repositories.PasswordRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAuthService implements UserDetailsService {

    private PasswordRepository passwordRepository;

    private AccountRepository accountRepository;
    @Autowired
    private UserAuthService (PasswordRepository userRepository,
                             AccountRepository accountRepository){
        this.passwordRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Password password = passwordRepository.findByAccountName(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found:"));

        return new User(password.getAccountName(), password.getPassword(), new ArrayList<>());
    }

}
