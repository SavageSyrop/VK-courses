package ru.vk;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.impl.JavaVerticleFactory;
import ru.vk.data.ClanData;
import ru.vk.data.Names;
import ru.vk.data.Paths;

import java.util.concurrent.Callable;

public class ClanVerticle extends AbstractVerticle {
    private ClanData clanData;

    public ClanVerticle() {
        this.clanData = new ClanData();
    }

    @Override
    public void start(Promise<Void> promise) {
        vertx.sharedData().getCounter(Names.CLAN_COUNTER.getValue(), counter -> {
            if (counter.succeeded()) {
                counter.result().incrementAndGet(number -> {
                    this.clanData.setId(number.result().intValue());
                    setAdmin().completionHandler(result -> {
                        if (putClanInSharedMap().succeeded()) {
                            promise.complete();
                        } else {
                            promise.fail(result.cause());
                        }
                    });
                });
            } else {
                promise.fail(counter.cause());
            }
        });
    }

    private MessageConsumer<Integer> setAdmin() {
        MessageConsumer<Integer> consumer = vertx.eventBus().<Integer>consumer(Paths.CLAN_SET_ADMIN.getValue() + this.clanData.getId());
        consumer.handler(event -> {
            consumer.pause();
            Integer adminId = event.body();
            vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map -> {
                map.result().get(clanData.getId(), result -> {
                    ClanData data = result.result();
                    if (data.getAdminId() != null) {
                        event.fail(-1, "Clan already has admin" + data.getAdminId());
                        consumer.resume();
                    } else {
                        data.setAdminId(adminId);
                        map.result().put(clanData.getId(), data, complete -> {
                            event.reply("Clan " + clanData.getId() + " is now under admin " + adminId);
                            consumer.resume();
                        });
                    }
                });
            });
        });
        return consumer;
    }

    private Future<Void> putClanInSharedMap() {
        Future<Void> future = Future.future(promise -> {
            vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map -> {
                map.result().put(clanData.getId(), clanData, result -> {
                    if (result.succeeded()) {
                        System.out.println("Clan " + clanData.getId() + " is created!");
                        promise.complete();
                    } else {
                        System.out.println("Failed to put clan in map");
                        promise.fail(result.cause());
                    }
                });
            });
        });

        return future.compose(result -> Future.<Void>future(promise -> {             // TODO понять возвращаемое значение
            vertx.eventBus().publish(Paths.CLAN_CREATED.getValue() + clanData.getId(), null);
            promise.complete();
        }));
    }


    public static final class Factory extends JavaVerticleFactory {


        @Override
        public String prefix() {
            return "Clan";
        }


        @Override
        public void createVerticle(String verticleName,
                                   ClassLoader classLoader,
                                   Promise<Callable<Verticle>> promise) {
            promise.complete(ClanVerticle::new);
        }
    }
}
