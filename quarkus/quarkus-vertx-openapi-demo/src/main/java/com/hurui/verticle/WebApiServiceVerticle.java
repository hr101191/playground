package com.hurui.verticle;

import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import javax.enterprise.context.Dependent;

@Dependent
public class WebApiServiceVerticle extends AbstractVerticle {

    public void start(Promise<Void> startPromise) throws Exception {
        startPromise.complete();
    }
}
