package com.hurui.verticle;

import io.quarkus.runtime.StartupEvent;
import io.vertx.core.DeploymentOptions;
import io.vertx.mutiny.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;

@ApplicationScoped
public class VertxDeployer {

    private static final Logger logger = LoggerFactory.getLogger(VertxDeployer.class);

    private final Vertx vertx;
    private final Instance<GraphQLServerVerticle> graphQLServerVerticleInstance;

    public VertxDeployer(Vertx vertx, Instance<GraphQLServerVerticle> graphQLServerVerticleInstance) {
        this.vertx = vertx;
        this.graphQLServerVerticleInstance = graphQLServerVerticleInstance;
    }

    void deployVerticles(@Observes StartupEvent startupEvent) {
        VertxDeployerUtil.deployVerticle(this.vertx, this.graphQLServerVerticleInstance, 4)
                .subscribe()
                .with(deploymentId -> {
                    logger.info("GraphQL Server Verticle deployed successfully. Deployment ID: {}", deploymentId);
                }, throwable -> {
                    logger.error("Failed to deploy GraphQL Server Verticle. Stacktrace: ", throwable);
                });
    }
}
