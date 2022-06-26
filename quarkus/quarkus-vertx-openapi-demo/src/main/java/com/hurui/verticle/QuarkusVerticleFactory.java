package com.hurui.verticle;

import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.spi.VerticleFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.Callable;

@ApplicationScoped
public class QuarkusVerticleFactory implements VerticleFactory {

    private final io.vertx.mutiny.core.Vertx vertx;

    public QuarkusVerticleFactory(io.vertx.mutiny.core.Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void init(Vertx vertx) {
        VerticleFactory.super.init(vertx);
    }

    @Override
    public String prefix() {
        return null;
    }

    @Override
    public void createVerticle(String s, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {

    }
}
