package com.babel.test.service;

public interface EventService {

	void sendEventMsg(String queue, String key, String msg);
}
