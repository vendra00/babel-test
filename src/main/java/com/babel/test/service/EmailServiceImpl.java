package com.babel.test.service;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service()
public class EmailServiceImpl implements EmailService{

	private static final String NOREPLY_ADDRESS = "noreply@babel.com";

	@Autowired
	private JavaMailSender emailSender;

	
	private SimpleMailMessage template;

	@Autowired
	private SpringTemplateEngine thymeleafTemplateEngine;

	public void sendSimpleMessage(String to, String subject, String text) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(NOREPLY_ADDRESS);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);

			emailSender.send(message);
		} catch (MailException exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void sendSimpleMessageUsingTemplate(String to, String subject, String... templateModel) {
		String text = String.format(template.getText(), templateModel);
		sendSimpleMessage(to, subject, text);
	}

	@Override
    public void sendMessageUsingThymeleafTemplate(
        String to, String subject, Map<String, Object> templateModel)
            throws MessagingException {
                
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        
        String htmlBody = thymeleafTemplateEngine.process("template-thymeleaf.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }
	
	private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(NOREPLY_ADDRESS);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        emailSender.send(message);
    }

}
