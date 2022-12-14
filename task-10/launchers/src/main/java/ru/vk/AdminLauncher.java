package ru.vk;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class AdminLauncher {
    public static void main(String[] args) {
        launchAdminVerticle(1, 6, 3);
    }

    private static void launchAdminVerticle(Integer clanId, Integer capacityUser, Integer capacityModerator) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                result -> {
                    Vertx vertx = result.result();
                    AdminVerticle.Factory factory = new AdminVerticle.Factory(clanId, capacityUser, capacityModerator);
                    vertx.registerVerticleFactory(factory);
                    DeploymentOptions options = new DeploymentOptions().setWorker(true);
                    vertx.deployVerticle(factory.prefix() + ":" + AdminVerticle.class.getSimpleName(), options, print -> {
                        System.out.println("Admin deploy result: " + print.succeeded());
                    });
                }
        );
    }
}