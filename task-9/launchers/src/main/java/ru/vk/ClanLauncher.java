package ru.vk;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class ClanLauncher {
    public static void main(String[] args) {
        launchClanVerticle(1);
    }

    private static void launchClanVerticle(int amount) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                result -> {
                    Vertx vertx = result.result();
                    ClanVerticle.Factory factory = new ClanVerticle.Factory();
                    vertx.registerVerticleFactory(factory);
                    DeploymentOptions options = new DeploymentOptions().setWorker(true).setInstances(amount);
                    vertx.deployVerticle(factory.prefix() + ":" + ClanVerticle.class.getSimpleName(), options, print -> {
                        System.out.println("Clan deploy result: " + print.succeeded());
                    });
                }
        );
    }
}
