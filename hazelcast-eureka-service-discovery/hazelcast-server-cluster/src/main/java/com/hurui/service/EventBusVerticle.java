package com.hurui.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Scope(SCOPE_PROTOTYPE)
public class EventBusVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventBusVerticle.class);
	
	@Override
	public void start() throws Exception {
		vertx.eventBus().consumer("queue", messageHandler -> {
			LOGGER.info("Queue: {} | Received message: {}", messageHandler.address(), messageHandler.body());
			messageHandler.reply(new JsonObject().put("key", "value"));
		});
		vertx.eventBus().consumer("topic", messageHandler -> {
			try {
				LOGGER.info("Topic: {} | Received message: {}", messageHandler.address(), messageHandler.body());
			} catch(Exception ex) {
				LOGGER.error("Error: ", ex);
			}

		});
	}
}
