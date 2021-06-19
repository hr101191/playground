package com.hurui.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public interface VertxMessagingService {

	<T> void publish(String address, T message);

	<T> void publish(String address, T message, DeliveryOptions deliveryOptions);

	<T> void send(String address, T message);

	<T> void send(String address, T message, DeliveryOptions deliveryOptions);

	Message<JsonObject> request(String address, JsonObject message)
			throws InterruptedException, ExecutionException, TimeoutException;

	Message<JsonObject> request(String address, JsonObject message, DeliveryOptions deliveryOptions)
			throws InterruptedException, ExecutionException, TimeoutException;

	<T, U> Message<U> request(String address, T message, Class<U> returnedMessageClass)
			throws InterruptedException, ExecutionException, TimeoutException;

	<T, U> Message<U> request(String address, T message, DeliveryOptions deliveryOptions, Class<U> returnedMessageClass)
			throws InterruptedException, ExecutionException, TimeoutException;

}
