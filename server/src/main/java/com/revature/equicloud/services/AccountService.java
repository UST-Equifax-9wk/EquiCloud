package com.revature.equicloud.services;


import com.revature.equicloud.dtos.AuthenticationResponse;
import com.revature.equicloud.entities.Account;
import com.revature.equicloud.entities.Password;
import com.revature.equicloud.exceptions.InvalidInputException;
import com.revature.equicloud.exceptions.NoAccountException;
import com.revature.equicloud.repositories.AccountRepository;
import com.revature.equicloud.repositories.PasswordRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional(Transactional.TxType.REQUIRED)
public class AccountService {
    private PasswordRepository passwordRepository;
    private AccountRepository accountRepository;

    @Autowired
    AccountService(PasswordRepository passwordRepository, AccountRepository accountRepository){
        this.accountRepository=accountRepository;
        this.passwordRepository=passwordRepository;
    }

    public AuthenticationResponse findAccount(String accountName) {
        Account account = accountRepository.findByAccountName(accountName);

        if (account == null) {
            throw new EntityNotFoundException("Account not found");
        }
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(account.getAccountName(),
                account.getEmail(),
                account.getFirstName(),
                account.getLastName());

        return authenticationResponse;
    }

}
