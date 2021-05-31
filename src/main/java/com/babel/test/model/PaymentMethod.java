package com.babel.test.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "PaymentMethod")
@Table(name = "payment_method")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class PaymentMethod {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_methods_id")
	private Long id;

	@ManyToOne()
	@JoinColumn(name = "user_id")
	private User user;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Transaction> transactionList;

	@Column(name = "payment_name", nullable = false, columnDefinition = "TEXT")
	private String paymentName;

}
