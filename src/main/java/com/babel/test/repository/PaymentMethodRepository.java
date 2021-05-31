package com.babel.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babel.test.model.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>{
	
	PaymentMethod findPaymentMethodByPaymentName(String paymentName);

}
