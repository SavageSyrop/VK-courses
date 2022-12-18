package ru.vk;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.impl.JavaVerticleFactory;
import ru.vk.data.ClanData;
import ru.vk.data.Names;
import ru.vk.data.Paths;

import java.util.List;
import java.util.concurrent.Callable;

public class ModeratorVerticle extends AbstractVerticle {
    private int id;
    private final Integer clanId;

    public ModeratorVerticle(Integer clanId) {
        this.clanId = clanId;
    }

    @Override
    public void start() {
        vertx.sharedData().getCounter(Names.MODERATOR_COUNTER.getValue(), counter -> {
            if (counter.succeeded()) {
                counter.result().incrementAndGet(number -> {
                    this.id = number.result().intValue();
                    vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map -> {
                        map.result().entries(clans -> {
                            if (clans.result().isEmpty()) {
                                System.out.println("There are no clans");
                                return;
                            }
                            if (clans.result().containsKey(clanId) && clans.result().get(clanId).getAdminId() != null) {
                                joinClanAsModerator();
                                acceptUsers();
                            } else {
                                vertx.eventBus().consumer(Paths.CLAN_SEARCH_FOR_MODERATOR.getValue() + clanId, event -> {
                                    joinClanAsModerator();
                                    acceptUsers();
                                });
                            }
                        });
                    });
                });
            }
        });
    }

    private void joinClanAsModerator() {
        System.out.println("Moderator " + id + " tries to join clan " + clanId);
        vertx.eventBus().request(Paths.ADMIN_ADD_MODERATOR.getValue() + clanId, id, response -> {
            if (response.succeeded()) {
                System.out.println(response.result().body());
                vertx.sharedData().<Integer, Integer>getAsyncMap(Names.MODERATOR_MAP.getValue(), map -> {
                    map.result().put(id, clanId);
                });
            } else {
                System.out.println("Unable to add moderator: " + response.cause());
            }
        });
    }

    private void acceptUsers() {
        MessageConsumer<Integer> consumer = vertx.eventBus().consumer(Paths.CLAN_MEMBER_JOIN.getValue() + clanId);
        consumer.handler(event -> {
            consumer.pause();
            Integer userId = event.body();
            vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map -> {
                map.result().get(clanId, clanInfo -> {
                    ClanData data = clanInfo.result();
                    if (data.getMemberUser().size() < data.getCapacityUser()) {
                        List<Integer> userMembers = data.getMemberUser();
                        userMembers.add(userId);
                        System.out.println(clanId + ": " + userMembers);
                        data.setMemberUser(userMembers);
                        map.result().put(clanId, data, result -> {
                            event.reply("User " + userId + " joined clan " + clanId + ". Accepted by moderator " + id);
                            consumer.resume();
                        });
                    } else {
                        vertx.eventBus().send(Paths.CLAN_OVERFLOW.getValue() + clanId, null);
                        event.fail(-1, "Maximum capacity of users is reached!");
                        consumer.resume();
                    }
                });
            });
        });
    }

    public static final class Factory extends JavaVerticleFactory {
        private final Integer clanId;

        public Factory(Integer clanId) {
            this.clanId = clanId;
        }

        @Override
        public String prefix() {
            return "Moderator_";
        }


        @Override
        public void createVerticle(String verticleName,
                                   ClassLoader classLoader,
                                   Promise<Callable<Verticle>> promise) {
            promise.complete(() -> new ModeratorVerticle(clanId));
        }
    }
}
