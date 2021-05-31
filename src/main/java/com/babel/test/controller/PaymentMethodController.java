package com.babel.test.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.babel.test.dto.PaymentMethodDto;
import com.babel.test.exceptions.ResourceNotFoundException;
import com.babel.test.model.PaymentMethod;
import com.babel.test.service.PaymentMethodService;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/payment-methods")
public class PaymentMethodController {
	
	@Autowired
	private PaymentMethodService paymentMethodService;
	
	@Cacheable(value = "listAllPaymentMethods")
	@GetMapping("/find-all-payment-methods")
	public ResponseEntity<List<PaymentMethodDto>> getAllPaymentMethods() {
		List<PaymentMethod> paymentMethods = paymentMethodService.getPaymentMethods();
		return ResponseEntity.ok(PaymentMethodDto.converter(paymentMethods));
	}
	
	@Cacheable(value = "listAllPaymentMethods")
	@GetMapping("/find-all-payment-methods-page")
	public ResponseEntity<Page<PaymentMethodDto>> getAllPaymentMethodsPageable(@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		Page<PaymentMethod> paymentMethods = paymentMethodService.getPaymentMethods(pageable);
		return ResponseEntity.ok(PaymentMethodDto.convertPage(paymentMethods));
	}
	
	@GetMapping({ "/find-payment-method-by-id/{paymentMethodId}" })
	public ResponseEntity<PaymentMethodDto> getPaymentMethodById(@PathVariable Long paymentMethodId) throws ResourceNotFoundException{	
		Optional<PaymentMethod> paymentMethod = Optional.of(paymentMethodService.getPaymentMethodById(paymentMethodId));
		if (paymentMethod.isPresent()) {
			return ResponseEntity.ok(new PaymentMethodDto(paymentMethod.get()));
		}
		return ResponseEntity.notFound().build();
	}
	
	@CacheEvict(value = "listAllPaymentMethods", allEntries = true)
	@Transactional
	@PostMapping({ "/insert-payment-method/" })
	public ResponseEntity<PaymentMethod> addPaymentMethod(@RequestBody PaymentMethod newPaymentMethod) {
		PaymentMethod paymentMethodBody = paymentMethodService.insertPaymentMethod(newPaymentMethod);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("payment-methods", "/payment-methods/insert-payment-method/" + paymentMethodBody.getId().toString());
		return new ResponseEntity<>(paymentMethodBody, httpHeaders, HttpStatus.CREATED);
	}
	
	@CacheEvict(value = "listAllPaymentMethods", allEntries = true)
	@Transactional
	@PutMapping({ "/update-user/{userId}" })
	public ResponseEntity<PaymentMethod> updatePaymentMethod(@PathVariable("paymentMethodId") Long paymentMethodId, @RequestBody PaymentMethod paymentMethod) throws ResourceNotFoundException{
		paymentMethodService.updatePaymentMethod(paymentMethodId, paymentMethod);
		return new ResponseEntity<>(paymentMethodService.getPaymentMethodById(paymentMethodId), HttpStatus.OK);
	}
	
	@CacheEvict(value = "listAllPaymentMethods", allEntries = true)
	@Transactional
	@DeleteMapping({ "/delete-payment-method/{paymentMethodId}" })
	public ResponseEntity<PaymentMethod> deletePaymentMethod(@PathVariable("paymentMethodId") Long paymentMethodId) throws ResourceNotFoundException{
		paymentMethodService.deletePaymentMethod(paymentMethodId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
