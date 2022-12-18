package ru.vk.data;

import lombok.Getter;

@Getter
public enum Names {
    CLAN_MAP ("clanMap"),   // id / clanInfo
    ADMIN_MAP("adminMap"), // id / clanId
    MODERATOR_MAP("moderatorMap"),
    USER_COUNTER("userCounter"),
    MODERATOR_COUNTER("moderatorCounter"),
    CLAN_COUNTER("clanCounter"),
    ADMIN_COUNTER("adminCounter");
    private final String value;

    Names(String value) {
        this.value = value;
    }
}
