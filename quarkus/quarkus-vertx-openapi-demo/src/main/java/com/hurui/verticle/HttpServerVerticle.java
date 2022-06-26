package com.hurui.verticle;

import com.hurui.handler.HttpFailureHandler;
import com.hurui.web.TestWebApiService;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import io.vertx.json.schema.openapi3.OpenAPI3SchemaParser;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.api.service.RouteToEBServiceHandler;
import io.vertx.mutiny.ext.web.handler.StaticHandler;
import io.vertx.mutiny.ext.web.handler.TimeoutHandler;
import io.vertx.mutiny.ext.web.openapi.RouterBuilder;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;

import static io.vertx.ext.web.validation.builder.Parameters.optionalParam;
import static io.vertx.json.schema.common.dsl.Schemas.intSchema;

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
                .setAddress("test")
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
                    routerBuilder.operation("listPets")
                            .failureHandler(this.httpFailureHandler::handle)
                            .routeToEventBus("test", new DeliveryOptions().setSendTimeout(100L));
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
