package com.hurui.verticle;

import com.hurui.entity.Post;
import com.hurui.service.PostService;
import io.quarkus.arc.Arc;
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
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class VertxDeployer {

    private static final Logger logger = LoggerFactory.getLogger(VertxDeployer.class);

    private final Vertx vertx;
    private final QuarkusVerticleFactory quarkusVerticleFactory;
    private final Instance<HttpServerVerticle> httpServerVerticleInstance;
    private final Instance<WebApiServiceVerticle> webApiServiceVerticleInstance;

    public VertxDeployer(Vertx vertx, QuarkusVerticleFactory quarkusVerticleFactory, Instance<HttpServerVerticle> httpServerVerticleInstance, Instance<WebApiServiceVerticle> webApiServiceVerticleInstance) {
        this.vertx = vertx;
        this.quarkusVerticleFactory = quarkusVerticleFactory;
        this.httpServerVerticleInstance = httpServerVerticleInstance;
        this.webApiServiceVerticleInstance = webApiServiceVerticleInstance;
    }

    void deployVerticles(@Observes StartupEvent startupEvent) {
        //this.vertx.registerVerticleFactory(this.quarkusVerticleFactory);
        this.vertx.deployVerticle(this.webApiServiceVerticleInstance.get(), new DeploymentOptions().setInstances(1).setWorker(Boolean.TRUE))
                .subscribe()
                .with(deploymentId -> {
                    logger.info("Web Api Service Verticle deployed successfully. Deployment ID: {}", deploymentId);
                }, throwable -> {
                    logger.error("Failed to deploy Http Server Verticle. Stacktrace: ", throwable);
                });
        this.vertx.deployVerticle(this.httpServerVerticleInstance.get(), new DeploymentOptions().setInstances(1).setWorker(Boolean.TRUE))
                .subscribe()
                .with(deploymentId -> {
                    logger.info("Http Server Verticle deployed successfully. Deployment ID: {}", deploymentId);
                }, throwable -> {
                    logger.error("Failed to deploy Http Server Verticle. Stacktrace: ", throwable);
                });
//        final Set<Bean<?>> beans = Arc.container().beanManager().getBeans(Object.class);
//        for (final Bean<?> bean : beans) {
//            logger.info("Fetched: " + bean.getBeanClass().getName() + " Scope: " + bean.getScope() + " bean name: " + bean.getName());
//        }
        //logger.info("bean by name: " + this.httpServerVerticle.getClass().getAnnotation(Named.class).value());
//        final Set<Bean<?>> beans1 = Arc.container().beanManager().getBeans("httpServerVerticle");
//        for (final Bean<?> bean : beans1) {
//            logger.info("Fetched: " + bean.getBeanClass().getName() + " Scope: " + bean.getScope() + " bean name: " + bean.getName());
//        }
    }
}
