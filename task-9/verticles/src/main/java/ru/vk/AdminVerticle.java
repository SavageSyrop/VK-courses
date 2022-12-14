package ru.vk;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.impl.JavaVerticleFactory;
import ru.vk.data.ClanData;
import ru.vk.data.Names;
import ru.vk.data.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AdminVerticle extends AbstractVerticle {
    private int id;
    private final Integer clanId;
    private final Integer capacityUser;
    private final Integer capacityModerator;

    public AdminVerticle(Integer clanId, Integer capacityUser, Integer capacityModerator) {
        this.clanId = clanId;
        this.capacityUser = capacityUser;
        this.capacityModerator = capacityModerator;
    }

    @Override
    public void start() {
        vertx.sharedData().getCounter("adminCounter", counter -> {
            if (counter.succeeded()) {
                counter.result().incrementAndGet(number -> {
                    this.id = number.result().intValue();
                    subscribeClanCreation();
                    addClanModerator();
                    checkClanUserCapacity();
                });
            }
        });
    }

    private void subscribeClanCreation() {
        vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map ->
        {
            System.out.println(0);
            map.result().entries(clans -> {
                if (clans.result().containsKey(clanId)) {
                    joinClanAsAdmin();
                } else {
                    vertx.eventBus().consumer(Paths.CLAN_CREATED.getValue() + clanId, event -> joinClanAsAdmin());
                }
            });
        });
    }

    private void joinClanAsAdmin() {
        System.out.println("Admin " + id + " tries to join clan " + clanId);
        vertx.eventBus().request(Paths.CLAN_SET_ADMIN.getValue() + clanId, id, response -> {
            if (response.succeeded()) {
                vertx.sharedData().<Integer, Integer>getAsyncMap(Names.ADMIN_MAP.getValue(), map -> {
                    map.result().put(id, clanId, added -> {
                        System.out.println("Admin " + id + " is added in clan " + clanId);
                    });
                });
                System.out.println(response.result().body());
                vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map -> {
                    map.result().entries(data -> {
                        ClanData clanData = data.result().get(clanId);
                        clanData.setCapacityUser(capacityUser);
                        clanData.setCapacityModerator(capacityModerator);
                        map.result().put(clanId, clanData);
                        System.out.println("Clan " + clanId + "  user capacity set to: " + capacityUser);
                        System.out.println("Clan " + clanId + " moderator capacity set to: " + capacityModerator);
                    });
                });
                vertx.eventBus().publish(Paths.CLAN_SET_ADMIN.getValue() + clanId, null);
            } else {
                System.out.println("Failed to join clan: " + response.cause().getMessage());
            }
        });
    }

    private void addClanModerator() {
        MessageConsumer<Integer> consumer = vertx.eventBus().<Integer>consumer(Paths.ADMIN_ADD_MODERATOR.getValue() + clanId);
        consumer.handler(event -> {
            consumer.pause();
            Integer moderatorId = event.body();
            vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map -> {
                map.result().get(clanId, data -> {
                    ClanData clan = data.result();
                    if (clan.getMemberModerator().size() < clan.getCapacityModerator()) {
                        List<Integer> moderators = data.result().getMemberModerator();
                        moderators.add(moderatorId);
                        clan.setMemberModerator(moderators);
                        map.result().put(clanId, clan, reply -> {
                            event.reply("Added moderator " + moderatorId + " to clan " + clanId);
                            consumer.resume();
                        });
                    } else {
                        event.fail(-1, "Moderator capacity overflow for clan " + clanId);
                        consumer.resume();
                    }
                });
            });
        });
    }

    private void checkClanUserCapacity() {
        vertx.eventBus().<Integer>consumer(Paths.CLAN_OVERFLOW.getValue() + clanId, event -> {
            vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map -> {
                map.result().get(clanId, clanData -> {
                    ClanData data = clanData.result();
                    data.setMemberUser(new ArrayList<>());
                    map.result().put(clanId, data);
                    System.out.println("Clan " + id + " exiled all users");
                    vertx.eventBus().publish(Paths.CLAN_REJOIN.getValue() + clanId, null);
                    System.out.println("Clan " + clanId + " sends rejoin!");
                });
            });
        });
    }

    public static final class Factory extends JavaVerticleFactory {
        private final Integer clanId;
        private final Integer capacityUser;
        private final Integer capacityModerator;


        public Factory(Integer clanId, Integer capacityUser, Integer capacityModerator) {
            this.clanId = clanId;
            this.capacityUser = capacityUser;
            this.capacityModerator = capacityModerator;
        }

        @Override
        public String prefix() {
            return "Admin";
        }


        @Override
        public void createVerticle(String verticleName,
                                   ClassLoader classLoader,
                                   Promise<Callable<Verticle>> promise) {
            promise.complete(() -> new AdminVerticle(clanId, capacityUser, capacityModerator));
        }
    }
}
