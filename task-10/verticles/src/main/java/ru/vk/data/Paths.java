package ru.vk.data;

import lombok.Getter;

@Getter
public enum Paths {
     CLAN_CREATED ("clan.created."),
     CLAN_SET_ADMIN ("clan.setAdmin."),
     CLAN_OVERFLOW("clan.user.overflow."),
     CLAN_REJOIN("clan.rejoin."),
     ADMIN_ADD_MODERATOR("admin.add_moderator."),
     CLAN_MEMBER_JOIN("clan.member.join."),
     USER_SEND_MESSAGE("user.send.message.");

     private final String value;

     Paths(String s) {
          this.value = s;
     }
}
