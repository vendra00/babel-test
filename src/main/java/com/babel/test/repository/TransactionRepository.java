package com.babel.test.repository;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.babel.test.model.Transaction;
import com.babel.test.model.TransactionStatus;
import com.babel.test.model.TransactionType;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	Transaction findByType (Enum<TransactionStatus> transactionType);
	
	Transaction findByStatus (Enum<TransactionType> transactionStatus);
	
	Transaction findByCreatedAt (Timestamp createdAt);
	
	Transaction findByExecuteAt (Timestamp createdAt);
}
