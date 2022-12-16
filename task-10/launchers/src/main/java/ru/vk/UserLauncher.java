package ru.vk;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class UserLauncher {
    public static void main(String[] args) {
        launchUserVericle(3000);
        launchUserVericle(4000);
        launchUserVericle(5000);
        launchUserVericle(6000);
        launchUserVericle(7000);
        launchUserVericle(8000);
        launchUserVericle(9000);
        launchUserVericle(10000);
        launchUserVericle(11000);

    }

    public static void launchUserVericle(int messageTimeout) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                result -> {
                    Vertx vertx = result.result();
                    UserVerticle.Factory factory = new UserVerticle.Factory(messageTimeout);
                    vertx.registerVerticleFactory(factory);
                    DeploymentOptions options = new DeploymentOptions().setWorker(true);
                    vertx.deployVerticle(factory.prefix() + ":" + UserVerticle.class.getSimpleName(), options, print -> {
                        System.out.println("User deploy result: " + print.succeeded());
                    });
                }
        );
    }
}
