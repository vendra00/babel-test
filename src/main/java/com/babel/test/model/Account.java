package com.babel.test.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Account")
@Table(name = "accounts", uniqueConstraints = {@UniqueConstraint(name = "account_account_number_unique", columnNames = "account_number") })
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "account_id")
	private Long id;
	
	@OneToOne(mappedBy = "account")
    private User user;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Transaction> transactionList;

	@Column(name = "account_number", nullable = false)
	private Integer accountNumber;
	
	@Column(name = "max_withdrawal_amount", nullable = false, columnDefinition = "Decimal(10,2) default '0.00'")
	private double maxWithdrawalAmount;

	@Column(name = "balance", nullable = false, columnDefinition = "Decimal(10,2) default '0.00'")
	private double balance;

}
