package com.hurui.verticle;

import com.hurui.web.PostWebApiService;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;

@Dependent
public class WebApiServiceVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(WebApiServiceVerticle.class);
    private final PostWebApiService postWebApiService;

    public WebApiServiceVerticle(PostWebApiService postWebApiService) {
        this.postWebApiService = postWebApiService;
    }

    public void start(Promise<Void> startPromise) throws Exception {
        MessageConsumer<JsonObject> messageConsumer = new ServiceBinder(this.vertx.getDelegate())
                .setAddress("x-vertx-event-bus-address-posts")
                .register(PostWebApiService.class, postWebApiService);
        messageConsumer.completionHandler(result -> {
            if(result.succeeded()) {
                logger.info("Successfully created service proxy.");
            } else {
                logger.error("Failed to create service proxy. Stacktrace: ", result.cause());
            }
        });
    }
}
