package com.babel.test.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Transaction")
@Table(name = "transactions")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id", updatable = false)
	private Long id;

	@Column(name = "comment", nullable = false, columnDefinition = "TEXT")
	private String comment;
	
	@Column(name = "amount", nullable = false, columnDefinition = "Decimal(10,2) default '0.00'")
	private double amount;
	
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE", updatable = false)
	Timestamp createdAt;

	@CreationTimestamp
	@Column(name = "execute_at", nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
	private Timestamp executeAt;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_status", nullable = false)
	private TransactionStatus status;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type", nullable = false, updatable = false)
	private TransactionType type;

	@ManyToOne
	@JoinColumn(name = "accountId")
	private Account account;
	
	@ManyToOne
	@JoinColumn(name = "paymentMethodId")
	private PaymentMethod paymentMethod;


}
