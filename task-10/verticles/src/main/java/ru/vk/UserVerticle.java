package ru.vk;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.impl.JavaVerticleFactory;
import io.vertx.ext.auth.VertxContextPRNG;
import ru.vk.data.ClanData;
import ru.vk.data.Names;
import ru.vk.data.Paths;
import ru.vk.data.UserData;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class UserVerticle extends AbstractVerticle {
    private int id;
    private UserData userData;
    private Long messageCounter = null;

    public UserVerticle(int messageTimeout) {
        this.userData = new UserData(null, messageTimeout);
    }

    @Override
    public void start() {
        vertx.sharedData().getCounter("userCounter", counter -> {
            if (counter.succeeded()) {
                counter.result().incrementAndGet(number -> {
                    this.id = number.result().intValue();
                    vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map -> {
                        map.result().entries(clans -> {
                            if (clans.result().isEmpty()) {
                                System.out.println("There are no clans");
                                return;
                            }
                            List<ClanData> clanDataList = clans.result().values().stream().filter(clanData -> Objects.nonNull(clanData.getAdminId())).collect(Collectors.toList());
                            if (clanDataList.size() > 0) {
                                int clanId = clanDataList.get(VertxContextPRNG.current().nextInt(clanDataList.size())).getId();
                                joinClan(clanId);
                                rejoinHandler(clanId);
                            }
                        });
                    });

                    vertx.eventBus().consumer(Paths.USER_SEND_MESSAGE.getValue() + id, event -> {
                        System.out.println("User " + id + " receives message: " + event.body());
                    });
                });
            }
        });
    }

    private void joinClan(int clanId) {
        System.out.println("User " + id + " tries to join clan " + clanId);
        if (userData.getClanId() == null) {
            vertx.eventBus().request(Paths.CLAN_MEMBER_JOIN.getValue() + clanId, id, response -> {
                if (response.succeeded()) {
                    userData.setClanId(clanId);
                    System.out.println(response.result().body());
                    messageCounter = sendMessageToRandomClanMember();
                } else {
                    System.out.println("Unable to join clan " + clanId + " Cause: " + response.cause());
                    System.out.println("User " + id + " request repeat time: " + userData.getMessageTimeout());
                    vertx.setPeriodic(userData.getMessageTimeout(), event -> {
                        if (VertxContextPRNG.current().nextBoolean()) {
                            joinClan(clanId);
                            vertx.cancelTimer(event);
                        }
                    });
                }
            });
        }
    }

    private void rejoinHandler(int clanId) {
        MessageConsumer<Integer> consumer = vertx.eventBus().consumer(Paths.CLAN_REJOIN.getValue() + clanId);
        consumer.handler(event -> {
            consumer.pause();
            System.out.println("User " + id + " needs to rejoin");
            vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map -> {
                map.result().entries(clans -> {
                    List<ClanData> clanDataList = clans.result().values().stream().filter(cl -> Objects.nonNull(cl.getAdminId())).collect(Collectors.toList());
                    if (clanDataList.size() > 0) {
                        userData.setClanId(null);
                        if (messageCounter != null) {
                            vertx.cancelTimer(messageCounter);
                        }
                        vertx.setTimer(userData.getMessageTimeout(), timer -> {
                            int newClanId = clanDataList.get(VertxContextPRNG.current().nextInt(clanDataList.size())).getId();
                            System.out.println("User " + id + " will rejoin to clan " + newClanId);
                            joinClan(newClanId);
                            rejoinHandler(newClanId);
                        });
                    }
                });
            });
        });
    }

    private Long sendMessageToRandomClanMember() {
        return vertx.setPeriodic(userData.getMessageTimeout(), event -> {
            vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map -> {
                map.result().entries(clans -> {
                    ClanData clanData = clans.result().get(userData.getClanId());
                    clanData.getMemberUser().remove((Integer) id);
                    if (clanData.getMemberUser().size() > 1) {
                        int memberId = clanData.getMemberUser().get(VertxContextPRNG.current().nextInt(clanData.getMemberUser().size()));
                        vertx.eventBus().send(Paths.USER_SEND_MESSAGE.getValue() + memberId, "Hi, i'm user " + id);
                    }
                });
            });
        });
    }


    public static final class Factory extends JavaVerticleFactory {

        private final int messageTimeout;

        public Factory(int messageTimeout) {

            this.messageTimeout = messageTimeout;
        }

        @Override
        public String prefix() {
            return "User_";
        }


        @Override
        public void createVerticle(String verticleName,
                                   ClassLoader classLoader,
                                   Promise<Callable<Verticle>> promise) {
            promise.complete(() -> new UserVerticle(messageTimeout));
        }
    }
}
