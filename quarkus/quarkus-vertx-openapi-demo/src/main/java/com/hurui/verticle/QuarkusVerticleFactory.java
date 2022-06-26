package com.hurui.verticle;

import io.quarkus.arc.Arc;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.spi.VerticleFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.Bean;
import java.util.Optional;
import java.util.concurrent.Callable;

@ApplicationScoped
public class QuarkusVerticleFactory implements VerticleFactory {

    private Vertx vertx;

    @Override
    public void init(Vertx vertx) {
        VerticleFactory.super.init(vertx);
        this.vertx = vertx;
    }

    @Override
    public String prefix() {
        return QuarkusVerticleFactory.class.getName();
    }

    @Override
    public void createVerticle(String verticleName, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {
        String clazz = VerticleFactory.removePrefix(verticleName);
        Future<Verticle> verticleFuture = this.vertx.executeBlocking(handler -> {
            try {
                Optional<Bean<?>> beansOptional = Arc.container().beanManager().getBeans(clazz).stream().findFirst();
                if(beansOptional.isPresent()) {
                    Verticle verticle = (Verticle) beansOptional.get();
                    handler.complete(verticle);
                } else {
                    handler.fail(new RuntimeException("Failed to retrieve bean from container. Bean name: " + clazz));
                }
            } catch (Exception ex) {
                handler.fail(ex);
            }
        });
        verticleFuture.onComplete(resultHandler -> {
            if(resultHandler.succeeded()) {
                promise.complete(resultHandler::result);
            } else {
                promise.fail(resultHandler.cause());
            }
        });

    }
}
