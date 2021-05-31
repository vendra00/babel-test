package com.babel.test.dto;

import java.sql.Timestamp;

import com.babel.test.model.Transaction;
import com.babel.test.model.TransactionStatus;
import com.babel.test.model.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTransactionDto {
	
	private String comment;
	private double amount;
	private Timestamp createdAt;
	private Timestamp executeAt;
	private TransactionType type;
	private TransactionStatus status;
	private Long accountId;
	private Long paymentMethodId;
	
	public CreateTransactionDto(Transaction transaction) {
		this.accountId = transaction.getAccount().getId();
		this.paymentMethodId = transaction.getPaymentMethod().getId();
		this.type = transaction.getType();
		this.status = transaction.getStatus();
		this.amount = transaction.getAmount();
		this.comment = transaction.getComment();
		this.createdAt = transaction.getCreatedAt();
		this.executeAt = transaction.getExecuteAt();
	}

}
