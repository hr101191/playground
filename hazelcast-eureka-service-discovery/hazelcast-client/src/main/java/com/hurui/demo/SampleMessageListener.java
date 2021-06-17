package com.hurui.demo;

import org.springframework.stereotype.Component;

import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;

@Component
public class SampleMessageListener implements MessageListener<String> {

	@Override
	public void onMessage(Message<String> message) {
		System.out.println("Message received. Data: " + message.getMessageObject());
	}

}
