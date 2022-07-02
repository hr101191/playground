package com.hurui.verticle;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.mutiny.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import java.util.ArrayList;
import java.util.List;

public class VertxDeployerUtil {

    private static final Logger logger = LoggerFactory.getLogger(VertxDeployerUtil.class);

    public static Uni<List<String>> deployVerticle(Vertx vertx, Instance<? extends AbstractVerticle> verticleInstance) {
        return deployVerticle(vertx, verticleInstance, new DeploymentOptions(), 1);
    }

    public static Uni<List<String>> deployVerticle(Vertx vertx, Instance<? extends AbstractVerticle> verticleInstance, int instances) {
        return deployVerticle(vertx, verticleInstance, new DeploymentOptions(), instances);
    }

    public static Uni<List<String>> deployVerticle(Vertx vertx, Instance<? extends AbstractVerticle> verticleInstance, DeploymentOptions deploymentOptions) {
        return deployVerticle(vertx, verticleInstance, deploymentOptions, 1);
    }

    public static Uni<List<String>> deployVerticle(Vertx vertx, Instance<? extends AbstractVerticle> verticleInstance, DeploymentOptions deploymentOptions, int instances) {
        List<Uni<String>> deploymentIdsUni = new ArrayList<>();
        for(int i = 0; i < instances; i++) {
            if(instances > 1) {
                logger.warn("Instance count in DeploymentOptions is > 1. Resetting instance count to 1. Please use method parameter [instances] to specify number of Verticle instance(s) to be deployed.");
                deploymentOptions.setInstances(1);
            }
            deploymentIdsUni.add(vertx.deployVerticle(verticleInstance.get(), deploymentOptions));
        }
        return Uni.join().all(deploymentIdsUni).andFailFast();
    }
}
