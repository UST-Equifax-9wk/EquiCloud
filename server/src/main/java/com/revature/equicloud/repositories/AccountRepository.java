package com.revature.equicloud.repositories;

import com.revature.equicloud.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByAccountId(Long accountId);
    void deleteByAccountId(Long accountId);
    Account findByAccountName(String accountName);

    Optional<Object> findByEmail(String email);
}
