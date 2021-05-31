package com.babel.test.controller;

import java.net.URI;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.babel.test.dto.CreateTransactionDto;
import com.babel.test.dto.TransactionDto;
import com.babel.test.exceptions.ResourceNotFoundException;
import com.babel.test.model.Transaction;
import com.babel.test.service.EmailService;
import com.babel.test.service.TransactionService;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/transaction")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
    public EmailService emailService;

	@Cacheable(value = "listAllTransactions")
	@GetMapping("/find-all-transactions")
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getTransactions();
        return ResponseEntity.ok(TransactionDto.converter(transactions));
    }
	
	@Cacheable(value = "listAllTransactions")
	@GetMapping("/find-all-transactions-page")
	public ResponseEntity<Page<TransactionDto>> getAllAccountsPageable(@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		Page<Transaction> transactions = transactionService.getTransactions(pageable);
		return ResponseEntity.ok(TransactionDto.convertPage(transactions));
	}
	
	@CacheEvict(value = "listAllTransactions", allEntries = true)
	@Transactional
	@PutMapping({"/update-transaction/{transactionId}"})
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("transactionId") Long transactionId, @RequestBody Transaction transaction) throws ResourceNotFoundException{
		transactionService.updateTransaction(transactionId, transaction);
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }
	
	@GetMapping({"/find-transactions-by-account-number/{accountNumber}"})
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountNumber(@PathVariable int accountNumber) {
		List<Transaction> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
		return ResponseEntity.ok(TransactionDto.converter(transactions));
    }
	
	@CacheEvict(value = "listAllTransactions", allEntries = true)
	@Transactional
	@PostMapping({ "/create-transaction/" })
	public ResponseEntity<CreateTransactionDto> addTransaction(@RequestBody Transaction newTransaction, UriComponentsBuilder uriBuilder) {
		Transaction transactionBody = transactionService.insertTransaction(newTransaction);
		URI uri = uriBuilder.path("/transaction/create-transaction/{id}").buildAndExpand(transactionBody.getId()).toUri();
		return ResponseEntity.created(uri).body(new CreateTransactionDto(transactionBody));
	}
	
	@GetMapping({ "/find-transaction-by-id/{transactionId}" })
	public ResponseEntity<Transaction> getTransactionById(@PathVariable Long transactionId) throws ResourceNotFoundException{	
		Optional<Transaction> transaction = Optional.of(transactionService.getTransactionById(transactionId));
		if (transaction.isPresent()) {
			return ResponseEntity.ok(transaction.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	

}
