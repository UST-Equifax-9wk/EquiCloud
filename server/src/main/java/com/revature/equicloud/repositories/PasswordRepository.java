package com.revature.equicloud.repositories;

import com.revature.equicloud.entities.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<Password,String> {
    Optional<Password> findByAccountName(String username);
}
