package com.babel.test.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.babel.test.model.PaymentMethod;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class PaymentMethodDto {
	
	private String userName;
	private String paymentName;
	
	public PaymentMethodDto(PaymentMethod paymentMethod) {
		this.userName = paymentMethod.getUser().getName();
		this.paymentName = paymentMethod.getPaymentName();
	}
	
	public static Page<PaymentMethodDto> convertPage(Page<PaymentMethod> paymentMethods) {
		return paymentMethods.map(PaymentMethodDto::new);
	}
	
	public static List<PaymentMethodDto> converter(List<PaymentMethod> paymentMethods) {
		return paymentMethods.stream().map(PaymentMethodDto::new).collect(Collectors.toList());
	}
}
