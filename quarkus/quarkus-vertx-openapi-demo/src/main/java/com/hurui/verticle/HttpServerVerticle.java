package com.hurui.verticle;

import com.hurui.web.HttpFailureHandler;
import com.hurui.web.TestWebApiService;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.handler.StaticHandler;
import io.vertx.mutiny.ext.web.openapi.RouterBuilder;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;

@Dependent//We want to create multiple instances of this verticle to be deployed
public class HttpServerVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

    private final HttpFailureHandler httpFailureHandler;
    private final TestWebApiService testWebApiService;

    public HttpServerVerticle(HttpFailureHandler httpFailureHandler, TestWebApiService testWebApiService) {
        this.httpFailureHandler = httpFailureHandler;
        this.testWebApiService = testWebApiService;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        MessageConsumer<JsonObject> messageConsumer = new ServiceBinder(this.vertx.getDelegate())
                .setAddress("x-vertx-event-bus-address-posts")
                .register(TestWebApiService.class, testWebApiService);
        messageConsumer.completionHandler(result -> {
            if(result.succeeded()) {
                logger.info("Successfully created service proxy.");
            } else {
                logger.error("Failed to create service proxy. Stacktrace: ", result.cause());
            }
        });

        RouterBuilder.create(this.vertx, "webroot/openapi.json")
                .flatMap(routerBuilder -> {
                    RouterBuilderOptions routerBuilderOptions = new RouterBuilderOptions();
                    routerBuilder.setOptions(routerBuilderOptions);
                    routerBuilder.operation("listPosts")
                            .failureHandler(this.httpFailureHandler::handle)
                            .routeToEventBus("x-vertx-event-bus-address-posts", new DeliveryOptions().setSendTimeout(100L));
                    Router router = routerBuilder.createRouter();
                    router.route().handler(StaticHandler.create("/webroot"));
                    HttpServer httpServer = this.vertx.createHttpServer();
                    return httpServer.requestHandler(router).listen(8080);
                })
                .subscribe()
                .with(httpServer -> {
                    logger.info("Http Server started on port: {}", httpServer.actualPort());
                    startPromise.complete();
                }, throwable -> {
                    logger.error("Failed to start Http Server. Stacktrace: ", throwable);
                    startPromise.fail(throwable);
                });
    }
}
