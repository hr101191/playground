package com.hurui.verticle;

import com.hurui.web.HttpFailureHandler;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.handler.StaticHandler;
import io.vertx.mutiny.ext.web.openapi.RouterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Dependent//We want to create multiple instances of this verticle to be deployed
@Named
public class HttpServerVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

    private final Router router;
    private final HttpFailureHandler httpFailureHandler;

    public HttpServerVerticle(Router router, HttpFailureHandler httpFailureHandler) {
        this.router = router;
        this.httpFailureHandler = httpFailureHandler;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        RouterBuilder.create(this.vertx, "webroot/openapi.json")
                .flatMap(routerBuilder -> {
                    RouterBuilderOptions routerBuilderOptions = new RouterBuilderOptions();
                    routerBuilder.setOptions(routerBuilderOptions);
                    routerBuilder.operation("listPosts")
                            .failureHandler(this.httpFailureHandler::handle)
                            .routeToEventBus("x-vertx-event-bus-address-posts", new DeliveryOptions().setSendTimeout(20000L));
                    routerBuilder.operation("createPosts")
                            .failureHandler(this.httpFailureHandler::handle)
                            .routeToEventBus("x-vertx-event-bus-address-posts", new DeliveryOptions().setSendTimeout(2000L));
                    Router vertxRouter = routerBuilder.createRouter();
                    vertxRouter.route().handler(StaticHandler.create());
                    router.mountSubRouter("/", vertxRouter);
                    return Uni.createFrom().voidItem();
                })
                .subscribe()
                .with(httpServer -> {
                    logger.info("Vertx OpenAPI routes mounted to Quarkus HTTP Server successfully.");
                    startPromise.complete();
                }, throwable -> {
                    logger.error("Failed to mount Vertx OpenAPI routes to Quarkus HTTP Server. Stacktrace: ", throwable);
                    startPromise.fail(throwable);
                });
    }
}
