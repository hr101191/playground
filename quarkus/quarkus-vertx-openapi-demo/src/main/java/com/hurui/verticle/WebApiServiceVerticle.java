package com.hurui.verticle;

import com.hurui.web.CommentWebApiService;
import com.hurui.web.PostWebApiService;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
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
    private final CommentWebApiService commentWebApiService;

    public WebApiServiceVerticle(PostWebApiService postWebApiService, CommentWebApiService commentWebApiService) {
        this.postWebApiService = postWebApiService;
        this.commentWebApiService = commentWebApiService;
    }

    public void start(Promise<Void> startPromise) throws Exception {
        MessageConsumer<JsonObject> postWebApiServiceMessageConsumer = new ServiceBinder(this.vertx.getDelegate())
                .setAddress("x-vertx-event-bus-address-posts")
                .register(PostWebApiService.class, this.postWebApiService);
        Promise<Void> postWebApiServicePromise = Promise.promise();
        postWebApiServiceMessageConsumer.completionHandler(result -> {
            if(result.succeeded()) {
                logger.info("Successfully created PostWebApiService proxy.");
                postWebApiServicePromise.complete(result.result());
            } else {
                logger.error("Failed to create PostWebApiService proxy. Stacktrace: ", result.cause());
                postWebApiServicePromise.fail(result.cause());
            }
        });
        MessageConsumer<JsonObject> commentWebApiServiceMessageConsumer = new ServiceBinder(this.vertx.getDelegate())
                .setAddress("x-vertx-event-bus-address-comments")
                .register(CommentWebApiService.class, this.commentWebApiService);
        Promise<Void> commentWebApiServicePromise = Promise.promise();
        commentWebApiServiceMessageConsumer.completionHandler(result -> {
            if(result.succeeded()) {
                logger.info("Successfully created CommentWebApiService proxy.");
                commentWebApiServicePromise.complete(result.result());
            } else {
                logger.error("Failed to create CommentWebApiService proxy. Stacktrace: ", result.cause());
                commentWebApiServicePromise.fail(result.cause());
            }
        });
        CompositeFuture.all(postWebApiServicePromise.future(), commentWebApiServicePromise.future())
                .onComplete(handler -> {
                    if(handler.succeeded()) {
                        startPromise.complete();
                    } else {
                        startPromise.fail(handler.cause());
                    }
                });
    }
}
