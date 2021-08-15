package com.example.CodeTest.repository;

import com.example.CodeTest.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {
}