package com.babel.test.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.babel.test.model.PaymentMethod;
import com.babel.test.repository.PaymentMethodRepository;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService{
	
	@Autowired
	PaymentMethodRepository paymentMethodRepository;

	@Override
	public PaymentMethod getPaymentMethodById(Long paymentMethodId) {
		return paymentMethodRepository.findById(paymentMethodId).get();
	}

	@Override
	public List<PaymentMethod> getPaymentMethods() {
		List<PaymentMethod> paymentsMethods = new ArrayList<>();
		paymentMethodRepository.findAll().forEach(paymentsMethods::add);
		return paymentsMethods;
	}

	@Override
	public Page<PaymentMethod> getPaymentMethods(Pageable pageable) {
		Page<PaymentMethod> paymentsMethods = paymentMethodRepository.findAll(pageable);
		return paymentsMethods;
	}

	@Override
	public PaymentMethod insertPaymentMethod(PaymentMethod paymentMethod) {
		return paymentMethodRepository.save(paymentMethod);
	}

	@Override
	public void updatePaymentMethod(Long PaymentMethodId, PaymentMethod paymentMethod) {
		PaymentMethod paymentsMethodFromDb = paymentMethodRepository.findById(PaymentMethodId).get();
		paymentsMethodFromDb.setPaymentName(paymentMethod.getPaymentName());
		paymentMethodRepository.save(paymentsMethodFromDb);
	}

	@Override
	public void deletePaymentMethod(Long paymentMethodId) {
		paymentMethodRepository.deleteById(paymentMethodId);
	}

}
