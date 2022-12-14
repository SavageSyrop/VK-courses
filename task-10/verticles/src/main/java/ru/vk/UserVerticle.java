package ru.vk;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.impl.JavaVerticleFactory;
import io.vertx.ext.auth.VertxContextPRNG;
import ru.vk.data.ClanData;
import ru.vk.data.Names;
import ru.vk.data.Paths;
import ru.vk.data.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class UserVerticle extends AbstractVerticle {
    private int id;
    private UserData userData;

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
                            List<ClanData> clanDataList = clans.result().values().stream().filter(clanData -> Objects.nonNull(clanData.getAdminId())).toList();
                            if (clanDataList.size() > 0) {
                                int clanId = clanDataList.get(VertxContextPRNG.current().nextInt(clanDataList.size())).getId();
                                joinClan(clanId);
                            }
                        });
                    });

                    vertx.eventBus().consumer(Paths.CLAN_REJOIN.getValue() + userData.getClanId(), event -> {
                        vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map -> {
                            map.result().entries(clans -> {
                                ClanData clanData = clans.result().get(userData.getClanId());
                                clanData.setMemberUser(new ArrayList<>());
                                map.result().put(userData.getClanId(), clanData);

                                List<ClanData> clanDataList = clans.result().values().stream().filter(cl -> Objects.nonNull(cl.getAdminId())).toList();
                                if (clanDataList.size()>0) {
                                    int clanId = clanDataList.get(VertxContextPRNG.current().nextInt(clanDataList.size())).getId();
                                    joinClan(clanId);
                                }
                            });
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
        vertx.eventBus().request(Paths.CLAN_MEMBER_JOIN.getValue() + clanId, id, response -> {
            if (response.succeeded()) {
                System.out.println(response.result().body());
                userData.setClanId(clanId);
                sendMessageToRandomClanMember();
            } else {
                System.out.println("Unable to join clan " + clanId + " Cause: " + response.cause());
                System.out.println("User " + id + " request repeat time: " + userData.getMessageTimeout());
                vertx.setTimer(userData.getMessageTimeout(), event -> {
                    if (VertxContextPRNG.current().nextBoolean()) {
                        joinClan(clanId);
                    }
                });
            }
        });
    }

    private void sendMessageToRandomClanMember() {
        vertx.setPeriodic(userData.getMessageTimeout(), event -> {
            vertx.sharedData().<Integer, ClanData>getAsyncMap(Names.CLAN_MAP.getValue(), map -> {
                map.result().entries(clans -> {
                    ClanData clanData = clans.result().get(userData.getClanId());
                    clanData.getMemberUser().remove((Integer) id);
                    if (clanData.getMemberUser().size() > 1) {
                        int memberId = VertxContextPRNG.current().nextInt(clanData.getMemberUser().size());
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
