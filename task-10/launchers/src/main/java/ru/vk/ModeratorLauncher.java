package ru.vk;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class ModeratorLauncher {
    public static void main(String[] args) {
        launchModeratorVerticle(1);
        launchModeratorVerticle(1);
        launchModeratorVerticle(2);
    }

    private static void launchModeratorVerticle(int clanId) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                result -> {
                    Vertx vertx = result.result();
                    ModeratorVerticle.Factory factory = new ModeratorVerticle.Factory(clanId);
                    vertx.registerVerticleFactory(factory);
                    DeploymentOptions options = new DeploymentOptions().setWorker(true);
                    vertx.deployVerticle(factory.prefix() + ":" + ModeratorVerticle.class.getSimpleName(), options, print -> {
                        System.out.println("Moderator deploy result: " + print.succeeded());
                    });
                }
        );
    }
}
