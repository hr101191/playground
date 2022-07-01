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
    private final Instance<HttpServerVerticle> httpServerVerticleInstance;
    private final Instance<WebApiServiceVerticle> webApiServiceVerticleInstance;

    public VertxDeployer(Vertx vertx, Instance<HttpServerVerticle> httpServerVerticleInstance, Instance<WebApiServiceVerticle> webApiServiceVerticleInstance) {
        this.vertx = vertx;
        this.httpServerVerticleInstance = httpServerVerticleInstance;
        this.webApiServiceVerticleInstance = webApiServiceVerticleInstance;
    }

    void deployVerticles(@Observes StartupEvent startupEvent) {
        //Verticle(s) running transactional services need to be deployed as worker
        VertxDeployerUtil.deployVerticle(this.vertx, this.webApiServiceVerticleInstance, new DeploymentOptions().setWorker(Boolean.TRUE), 4)
                .subscribe()
                .with(deploymentId -> {
                    logger.info("Web Api Service Verticle deployed successfully. Deployment ID: {}", deploymentId);
                }, throwable -> {
                    logger.error("Failed to deploy Http Server Verticle. Stacktrace: ", throwable);
                });
        //Verticle(s) not running transactional services can be deployed on a new vertx event loop
        VertxDeployerUtil.deployVerticle(this.vertx, this.httpServerVerticleInstance, 4)
                .subscribe()
                .with(deploymentId -> {
                    logger.info("Http Server Verticle deployed successfully. Deployment ID: {}", deploymentId);
                }, throwable -> {
                    logger.error("Failed to deploy Http Server Verticle. Stacktrace: ", throwable);
                });
    }
}
