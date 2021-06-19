package com.hurui.service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

//@Component
public class VertxMessagingServiceImpl implements VertxMessagingService, ApplicationListener<ApplicationStartedEvent> {
	
	private Vertx clusteredVertx;

	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		// Only created the vertx instance when spring context is fully started. This will prevent the autowired hazelcastInstance
		// from throwing NPE due to the initialization delay caused by the eureka service discovery. HazelcastInstance will only
		// complete initialization once it has gotten all the node data from eureka.
		ClusterManager clusterManager = new HazelcastClusterManager(hazelcastInstance);
		VertxOptions vertxOptions = new VertxOptions().setClusterManager(clusterManager);
		Vertx.clusteredVertx(vertxOptions, res -> {
			if(res.failed()) {
				//failed to construct vertx cluster
				res.cause().printStackTrace();
			} else {
				clusteredVertx = res.result();
				//Construct eventBus listeners below
				clusteredVertx.eventBus().consumer("queue", messageHandler -> {
					System.out.println("Received message: " + messageHandler.body());
					messageHandler.reply(new JsonObject().put("key", "value"));
				});
				clusteredVertx.eventBus().consumer("topic", messageHandler -> {
					System.out.println("Received message: " + messageHandler.body());
				});
			}
		});
	}
	
	@Override
	public <T> void publish(String address, T message) {
		//Always check if vertx cluster is not null before proceeding
		Optional<Vertx> clusteredVertxOptional = Optional.ofNullable(clusteredVertx);
		if(clusteredVertxOptional.isPresent()) {
			//Code can be synchronous or asynchronous depending on preference
			clusteredVertxOptional.get().eventBus().publisher(address).write(message, res -> {
				if(res.failed()) {
					//log error during publish
					res.cause().printStackTrace();
					//Optionally persist the message here (remember to use executeBlocking if method is blocking as code runs on vertx eventLoop here)
				} else {
					//Optionally log the message body	
					System.out.println("Message published to cluster successfully: " + message);
				}
			});
		} else {
			//Do nothing and warn that vertx cluster is not ready!
		}
	}

	@Override
	public <T> void publish(String address, T message, DeliveryOptions deliveryOptions) {
		//Always check if vertx cluster is not null before proceeding
		Optional<Vertx> clusteredVertxOptional = Optional.ofNullable(clusteredVertx);
		if(clusteredVertxOptional.isPresent()) {
			//Code can be synchronous or asynchronous depending on preference
			clusteredVertxOptional.get().eventBus().publisher(address, deliveryOptions).write(message, res -> {
				if(res.failed()) {
					//log error during publish
					res.cause().printStackTrace();
					//Optionally persist the message here (remember to use executeBlocking if method is blocking as code runs on vertx eventLoop here)
				} else {
					//Optionally log the message body
					System.out.println("Message published to cluster successfully: " + message);
				}
			});
		} else {
			//Do nothing and warn that vertx cluster is not ready!
		}
	}
	
	@Override
	public <T> void send(String address, T message) {
		//Always check if vertx cluster is not null before proceeding
		Optional<Vertx> clusteredVertxOptional = Optional.ofNullable(clusteredVertx);
		if(clusteredVertxOptional.isPresent()) {
			//Code can be synchronous or asynchronous depending on preference
			clusteredVertxOptional.get().eventBus().sender(address).write(message, res -> {
				if(res.failed()) {
					//log error during publish
					res.cause().printStackTrace();
					//Optionally persist the message here (remember to use executeBlocking if method is blocking as code runs on vertx eventLoop here)
				} else {
					//Optionally log the message body
					System.out.println("Message sent to cluster successfully: " + message);
				}
			});
		} else {
			//Do nothing and warn that vertx cluster is not ready!
		}
	}
	
	@Override
	public <T> void send(String address, T message, DeliveryOptions deliveryOptions) {
		//Always check if vertx cluster is not null before proceeding
		Optional<Vertx> clusteredVertxOptional = Optional.ofNullable(clusteredVertx);
		if(clusteredVertxOptional.isPresent()) {
			//Code can be synchronous or asynchronous depending on preference
			clusteredVertxOptional.get().eventBus().sender(address, deliveryOptions).write(message, res -> {
				if(res.failed()) {
					//log error during publish
					res.cause().printStackTrace();
					//Optionally persist the message here (remember to use executeBlocking if method is blocking as code runs on vertx eventLoop here)
				} else {
					//Optionally log the message body
					System.out.println("Message sent to cluster successfully: " + message);
				}
			});
		} else {
			//Do nothing and warn that vertx cluster is not ready!
		}
	}
	
	@Override
	public Message<JsonObject> request(String address, JsonObject message) throws InterruptedException, ExecutionException, TimeoutException {
		//Always check if vertx cluster is not null before proceeding
		//We want it to execute on the jvm ForkJoinPool instead of vertx event loop
		//This way, it causes the action of awaiting to be executed in the ForkJoinPool
		//instead of the vertx event loop to prevent blocking of the vertx event loop.
		CompletableFuture<Message<JsonObject>> completableFuture = new CompletableFuture<>();
		Optional.ofNullable(clusteredVertx)
			.orElseThrow() //throw exception if vertx is not ready
			.eventBus()
			.<JsonObject>request(address, message)
			.onComplete(handler -> {
				if(handler.failed()) {
					completableFuture.completeExceptionally(handler.cause());
				} else {
					completableFuture.complete(handler.result());
				}
			});
		//Finally, terminate the completable future. We set a threshold of 5 seconds longer than the event bus send timeout.
		//The completable future should never time out but just in case.
		return completableFuture.get(new DeliveryOptions().getSendTimeout() + 5, TimeUnit.SECONDS);
	}

	@Override
	public Message<JsonObject> request(String address, JsonObject message, DeliveryOptions deliveryOptions) throws InterruptedException, ExecutionException, TimeoutException {
		//We want it to execute on the jvm ForkJoinPool instead of vertx event loop
		//This way, it causes the action of awaiting to be executed in the ForkJoinPool
		//instead of the vertx event loop to prevent blocking of the vertx event loop.
		CompletableFuture<Message<JsonObject>> completableFuture = new CompletableFuture<>();
		Optional.ofNullable(clusteredVertx)
			.orElseThrow() //throw exception if vertx is not ready
			.eventBus()
			.<JsonObject>request(address, message, deliveryOptions)
			.onComplete(handler -> {
				if(handler.failed()) {
					completableFuture.completeExceptionally(handler.cause());
				} else {
					completableFuture.complete(handler.result());
				}
			});
		//Finally, terminate the completable future. We set a threshold of 5 seconds longer than the event bus send timeout.
		//The completable future should never time out but just in case.
		return completableFuture.get(deliveryOptions.getSendTimeout() + 5, TimeUnit.SECONDS);
	}
	
	@Override
	public <T, U> Message<U> request(String address, T message, Class<U> returnedMessageClass) throws InterruptedException, ExecutionException, TimeoutException {
		//Always check if vertx cluster is not null before proceeding
		//We want it to execute on the jvm ForkJoinPool instead of vertx event loop
		//This way, it causes the action of awaiting to be executed in the ForkJoinPool
		//instead of the vertx event loop to prevent blocking of the vertx event loop.
		CompletableFuture<Message<U>> completableFuture = new CompletableFuture<>();
		Optional.ofNullable(clusteredVertx)
			.orElseThrow() //throw exception if vertx is not ready
			.eventBus()
			.<U>request(address, message)
			.onComplete(handler -> {
				if(handler.failed()) {
					completableFuture.completeExceptionally(handler.cause());
				} else {
					completableFuture.complete(handler.result());
				}
			});
		//Finally, terminate the completable future. We set a threshold of 5 seconds longer than the event bus send timeout.
		//The completable future should never time out but just in case.
		return completableFuture.get(new DeliveryOptions().getSendTimeout() + 5, TimeUnit.SECONDS);
	}
	
	@Override
	public <T, U> Message<U> request(String address, T message, DeliveryOptions deliveryOptions, Class<U> returnedMessageClass) throws InterruptedException, ExecutionException, TimeoutException {
		//We want it to execute on the jvm ForkJoinPool instead of vertx event loop
		//This way, it causes the action of awaiting to be executed in the ForkJoinPool
		//instead of the vertx event loop to prevent blocking of the vertx event loop.
		CompletableFuture<Message<U>> completableFuture = new CompletableFuture<>();
		Optional.ofNullable(clusteredVertx)
			.orElseThrow() //throw exception if vertx is not ready
			.eventBus()
			.<U>request(address, message, deliveryOptions)
			.onComplete(handler -> {
				if(handler.failed()) {
					completableFuture.completeExceptionally(handler.cause());
				} else {
					completableFuture.complete(handler.result());
				}
			});
		//Finally, terminate the completable future. We set a threshold of 5 seconds longer than the event bus send timeout.
		//The completable future should never time out but just in case.
		return completableFuture.get(deliveryOptions.getSendTimeout() + 5, TimeUnit.SECONDS);
	}
	
}
