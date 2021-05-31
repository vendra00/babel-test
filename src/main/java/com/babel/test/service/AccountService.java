package com.babel.test.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.babel.test.model.Account;

public interface AccountService {

	Account getAccountById(Long accountId);
	
	Account getAccountByAccountNumber(int accountNumber);
	
	void updateAccount(int accountNumber, Account account);

	List<Account> getAccounts();

	Page<Account> findAll(Pageable pageable);
}
