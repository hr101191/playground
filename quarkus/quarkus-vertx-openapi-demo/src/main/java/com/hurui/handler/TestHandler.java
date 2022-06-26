package com.hurui.handler;

import io.vertx.core.Handler;
import io.vertx.mutiny.ext.web.RoutingContext;

import javax.enterprise.context.Dependent;

@Dependent
public class TestHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        routingContext.response().setStatusCode(200).endAndForget();
    }

}
