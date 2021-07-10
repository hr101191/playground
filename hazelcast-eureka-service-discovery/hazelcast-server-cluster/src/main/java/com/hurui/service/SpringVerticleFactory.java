package com.hurui.service;

import java.util.concurrent.Callable;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;

@Component
public class SpringVerticleFactory implements VerticleFactory, ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public String prefix() {
		return "spring-verticle-factory";
	}

	@Override
	public void createVerticle(String verticleName, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {
		String clazz = VerticleFactory.removePrefix(verticleName);
		promise.complete(() -> (Verticle) applicationContext.getBean(Class.forName(clazz)));
	}

}
