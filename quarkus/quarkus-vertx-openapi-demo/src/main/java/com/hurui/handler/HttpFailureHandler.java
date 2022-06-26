package com.hurui.handler;

import com.hurui.verticle.HttpServerVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.mutiny.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HttpFailureHandler implements Handler<RoutingContext> {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

    @Override
    public void handle(RoutingContext routingContext) {
        logger.error("Error code: {} | Error class: {} | Stacktrace: ", routingContext.statusCode(), routingContext.failure().getClass().getName(), routingContext.failure());
        if(!routingContext.response().ended()) {
            if(routingContext.failure() instanceof io.vertx.core.eventbus.ReplyException) {
                ReplyFailure failureType = ((ReplyException) routingContext.failure()).failureType();
                if(failureType.equals(ReplyFailure.TIMEOUT)) {
                    routingContext.response().setStatusCode(503).endAndForget();
                } else {
                    routingContext.response().setStatusCode(500).endAndForget();
                }
            } else {
                routingContext.response().setStatusCode(routingContext.statusCode()).endAndForget();
            }
        } else {
            logger.warn("In HttpFailureHandler. RoutingContext has already ended!");
        }
    }

}
