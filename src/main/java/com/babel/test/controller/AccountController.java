package com.babel.test.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.babel.test.dto.AccountDto;
import com.babel.test.exceptions.ResourceNotFoundException;
import com.babel.test.model.Account;
import com.babel.test.service.AccountService;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private AccountService accountrService;
	
	@Cacheable(value = "listAllAccounts")
	@GetMapping("/find-all-accounts")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<Account> accounts = accountrService.getAccounts();
        return ResponseEntity.ok(AccountDto.converter(accounts));
    }
	
	@Cacheable(value = "listAllAccounts")
	@GetMapping("/find-all-accounts-page")
	public ResponseEntity<Page<AccountDto>> getAllAccountsPageable(@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		Page<Account> accounts = accountrService.findAll(pageable);
		return ResponseEntity.ok(AccountDto.convertPage(accounts));
	}
	
	@GetMapping({"/find-account-by-account-number/{accountNumber}"})
    public ResponseEntity<AccountDto> getAccountByAccountNumber(@PathVariable int accountNumber) throws ResourceNotFoundException{
		Optional<Account> account = Optional.of(accountrService.getAccountByAccountNumber(accountNumber));
        if (account.isPresent()) {
			return ResponseEntity.ok(new AccountDto(account.get()));
		}
		return ResponseEntity.notFound().build();
    }
	
	@CacheEvict(value = "listAllAccounts", allEntries = true)
	@Transactional
	@PutMapping({"/update-account/{accountNumber}"})
    public ResponseEntity<Account> updateAccount(@PathVariable("accountNumber") int accountNumber, @RequestBody Account account) throws ResourceNotFoundException{
		accountrService.updateAccount(accountNumber, account);
        return ResponseEntity.ok(accountrService.getAccountByAccountNumber(accountNumber));
    }

}
