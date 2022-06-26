package com.hurui.verticle;

import io.quarkus.runtime.StartupEvent;
import io.vertx.core.DeploymentOptions;
import io.vertx.mutiny.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class VertxDeployer {

    private static final Logger logger = LoggerFactory.getLogger(VertxDeployer.class);

    private final Vertx vertx;
    private final HttpServerVerticle httpServerVerticle;

    public VertxDeployer(Vertx vertx, HttpServerVerticle httpServerVerticle) {
        this.vertx = vertx;
        this.httpServerVerticle = httpServerVerticle;
    }

    void deployVerticles(@Observes StartupEvent startupEvent) {
        this.vertx.deployVerticle(this.httpServerVerticle, new DeploymentOptions().setInstances(1))
                .subscribe()
                .with(deploymentId -> {
                    logger.info("Http Server Verticle deployed successfully. Deployment ID: {}", deploymentId);
                }, throwable -> {
                    logger.error("Failed to deploy Http Server Verticle. Stacktrace: ", throwable);
                });
    }
}
