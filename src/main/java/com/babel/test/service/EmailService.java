package com.babel.test.service;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;

public interface EmailService {

	void sendSimpleMessage(String to, String subject, String text);

	void sendSimpleMessageUsingTemplate(String to, String subject, String... templateModel);

	void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel)
			throws IOException, MessagingException;

}
