package com.babel.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.babel.test.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

	Account findAccountByAccountNumber(int accountNumber);
	
}
