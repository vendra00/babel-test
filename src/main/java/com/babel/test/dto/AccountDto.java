package com.babel.test.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.babel.test.model.Account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
	
	private int accountNumber;
	double maxWithdrawalAmount;
	private double balance;
	
	public AccountDto(Account account) {
		this.accountNumber = account.getAccountNumber();
		this.maxWithdrawalAmount = account.getMaxWithdrawalAmount();
		this.balance = account.getBalance();
		
	}
	
	public static Page<AccountDto> convertPage(Page<Account> accounts) {
		return accounts.map(AccountDto::new);
	}
	
	public static List<AccountDto> converter(List<Account> accounts) {
		return accounts.stream().map(AccountDto::new).collect(Collectors.toList());
	}

}
