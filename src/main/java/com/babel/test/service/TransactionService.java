package com.babel.test.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.babel.test.model.Transaction;

public interface TransactionService {

	Transaction getTransactionById(Long transactionId);

	Transaction getTransactionByDateCreated(Timestamp createdAt);
	
	Transaction getTransactionByDateExecuted(Timestamp executedAt);

	void updateTransaction(Long transactionid, Transaction transaction);

	List<Transaction> getTransactions();
	
	List<Transaction> getTransactionsByAccountNumber(int accountNumber);

	Page<Transaction> getTransactions(Pageable pageable);
	
	Transaction insertTransaction(Transaction transaction);

    void deleteTransaction(Long transactionId);
}
