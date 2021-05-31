package com.babel.test.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService{
	
	@Autowired
	EventService eventService;
	
	@Autowired
    private RabbitTemplate template;

	@Override
	@Async
	public void sendEventMsg(String queue, String key, String msg) {	
		template.convertAndSend(queue, key, msg);
	}

}
