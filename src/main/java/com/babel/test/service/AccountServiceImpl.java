package com.babel.test.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.babel.test.model.Account;
import com.babel.test.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	AccountRepository accountRepository;

	@Override
	public Account getAccountById(Long accountId) {
		return accountRepository.findById(accountId).get();
	}

	@Override
	public Account getAccountByAccountNumber(int accountNumber) {
		return accountRepository.findAccountByAccountNumber(accountNumber);
	}

	@Override
	public List<Account> getAccounts() {
		List<Account> accounts = new ArrayList<>();
		accountRepository.findAll().forEach(accounts::add);
		return accounts;
	}

	@Override
	public void updateAccount(int accountNumber, Account account) {
		Account accountFromDb = accountRepository.findAccountByAccountNumber(accountNumber);
		accountFromDb.setMaxWithdrawalAmount(account.getMaxWithdrawalAmount());
		accountFromDb.setBalance(account.getBalance());;
		accountRepository.save(accountFromDb);	
	}

	@Override
	public Page<Account> findAll(Pageable pageable) {
		Page<Account> accounts = accountRepository.findAll(pageable);
		return accounts;
	}
	
	

}
