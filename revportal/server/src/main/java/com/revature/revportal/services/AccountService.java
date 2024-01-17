package com.revature.revportal.services;


import com.revature.revportal.entities.Account;
import com.revature.revportal.entities.Password;
import com.revature.revportal.exceptions.InvalidInputException;
import com.revature.revportal.exceptions.NoAccountException;
import com.revature.revportal.repositories.AccountRepository;
import com.revature.revportal.repositories.PasswordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional(Transactional.TxType.REQUIRED)
public class AccountService {
    private PasswordRepository passwordRepository;
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    AccountService(PasswordRepository passwordRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder){
        this.accountRepository=accountRepository;
        this.passwordRepository=passwordRepository;
        this.passwordEncoder = passwordEncoder;
    }


//    public Account createDistributorAccount(DistributorAccountDto distributorAccountDto) throws InvalidInputException {
//        if(distributorAccountDto.getAccountName()==null ||
//                distributorAccountDto.getAccountName().length()<4||
//                distributorAccountDto.getDistributor()==null||
//                distributorAccountDto.getPassword()==null||
//                distributorAccountDto.getPassword().length()<8
//        )throw new InvalidInputException(InvalidInputException.invalidInput);
//        if(findAccountByName(distributorAccountDto.getAccountName())!=null) throw new InvalidInputException(InvalidInputException.duplicateUseOfUniqueAttribute);
//
//        Account account = new Account(distributorAccountDto.getAccountName(), true, null, distributorAccountDto.getDistributor(), null, null,null);
//        Password password = new Password(distributorAccountDto.getAccountName(),passwordEncoder.encode(distributorAccountDto.getPassword()));
//        distributorAccountDto.getDistributor().setAccount(account);
//        this.distributorService.createDistributer(distributorAccountDto.getDistributor());
//        this.accountRepository.save(account);
//        this.passwordRepository.save(password);
//        return account;
//    }
//
//
//    public UserAccountDto createUserAccount(UserAccountDto userAccountDto) throws InvalidInputException {
//        if(userAccountDto.getAccountName()==null ||
//                userAccountDto.getAccountName().length()<4||
//                userAccountDto.getUser()==null||
//                userAccountDto.getPassword()==null||
//                userAccountDto.getPassword().length()<8
//        )throw new InvalidInputException(InvalidInputException.invalidInput);
//        if(findAccountByName(userAccountDto.getAccountName())!=null) throw new InvalidInputException(InvalidInputException.duplicateUseOfUniqueAttribute);
//
//        Account account = new Account(userAccountDto.getAccountName(), false, userAccountDto.getUser(), null, null, null,null);
//
//        Password password = new Password(userAccountDto.getAccountName(),passwordEncoder.encode(userAccountDto.getPassword()));
//        userAccountDto.getUser().setAccount(account);
//        this.userService.createUser(userAccountDto.getUser());
//        this.accountRepository.save(account);
//        this.passwordRepository.save(password);
//        try {
//            cartService.createCart(userAccountDto.getAccountName());
//        } catch (NoAccountException e) {
//            throw new RuntimeException(e);
//        } catch (CartException e) {
//            throw new RuntimeException(e);
//        }
//        UserAccountDto result = new UserAccountDto(userAccountDto.getUser(),userAccountDto.getAccountName(),"Hidden");
//        return result;
//    }
//
//    public Account findAccountByName(String name){
//        if(name==null)return null;
//        return this.accountRepository.findByAccountName(name);
//    }
//
//
//    public Account retrieveAccount(String username) throws InvalidInputException {
//        if(username==null)return null;
//        Account account = this.accountRepository.findByAccountName(username);
//        if(account==null)throw new InvalidInputException(InvalidInputException.accountNotFound);
//        return account;
//    }
}
