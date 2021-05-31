package com.babel.test.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.babel.test.model.Transaction;
import com.babel.test.model.TransactionStatus;
import com.babel.test.model.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {
	
	private String comment;
	private double amount;
	private Timestamp createdAt;
	private Timestamp executeAt;
	private TransactionType type;
	private TransactionStatus status;
	private int accountNumber;
	private String paymentMethodName;
	
	public TransactionDto(Transaction transaction) {
		this.accountNumber = transaction.getAccount().getAccountNumber();
		this.paymentMethodName = transaction.getPaymentMethod().getPaymentName();
		this.type = transaction.getType();
		this.status = transaction.getStatus();
		this.amount = transaction.getAmount();
		this.comment = transaction.getComment();
		this.createdAt = transaction.getCreatedAt();
		this.executeAt = transaction.getExecuteAt();
	}
	
	public static Page<TransactionDto> convertPage(Page<Transaction> transactions) {
		return transactions.map(TransactionDto::new);
	}
	
	public static List<TransactionDto> converter(List<Transaction> transactions) {
		return transactions.stream().map(TransactionDto::new).collect(Collectors.toList());
	}
}
