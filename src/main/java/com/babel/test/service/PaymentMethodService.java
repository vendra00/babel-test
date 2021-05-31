package com.babel.test.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.babel.test.model.PaymentMethod;
import com.babel.test.model.User;

public interface PaymentMethodService {

	PaymentMethod getPaymentMethodById(Long PaymentMethodId);
	
	List<PaymentMethod> getPaymentMethods();
	
	Page<PaymentMethod> getPaymentMethods(Pageable pageable);

	PaymentMethod insertPaymentMethod(PaymentMethod paymentMethod);
	
	void updatePaymentMethod(Long PaymentMethodId, PaymentMethod paymentMethod);

    void deletePaymentMethod(Long paymentMethodId);
}
