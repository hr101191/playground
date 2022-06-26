package com.hurui.verticle;

import com.hurui.entity.Post;
import com.hurui.service.PostService;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.mutiny.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.stream.Collectors;

@ApplicationScoped
public class VertxDeployer {

    private static final Logger logger = LoggerFactory.getLogger(VertxDeployer.class);

    private final Vertx vertx;
    private final HttpServerVerticle httpServerVerticle;
    private final PostService postService;

    public VertxDeployer(Vertx vertx, HttpServerVerticle httpServerVerticle, PostService postService) {
        this.vertx = vertx;
        this.httpServerVerticle = httpServerVerticle;
        this.postService = postService;
    }

    void deployVerticles(@Observes StartupEvent startupEvent) {
        this.postService.listPosts(1)
                .runSubscriptionOn(Infrastructure.getDefaultExecutor())
                .subscribe()
                .with(result -> {

                }, throwable -> {

                });
        this.vertx.deployVerticle(this.httpServerVerticle, new DeploymentOptions().setInstances(1).setWorker(Boolean.TRUE))
                .subscribe()
                .with(deploymentId -> {
                    logger.info("Http Server Verticle deployed successfully. Deployment ID: {}", deploymentId);
                }, throwable -> {
                    logger.error("Failed to deploy Http Server Verticle. Stacktrace: ", throwable);
                });
    }
}
