package ru.vk.data;

import lombok.Getter;

@Getter
public enum Paths {
     CLAN_CREATED ("clan.created."),
     CLAN_SET_ADMIN ("clan.set_admin."),
     CLAN_SEARCH_FOR_MODERATOR("clan.search_for_moderator."),
     CLAN_OVERFLOW("clan.user_overflow."),
     CLAN_REJOIN("clan.rejoin."),
     ADMIN_ADD_MODERATOR("admin.add_moderator."),
     CLAN_MEMBER_JOIN("clan.member_join."),
     USER_SEND_MESSAGE("user.send_message.");

     private final String value;

     Paths(String s) {
          this.value = s;
     }
}
