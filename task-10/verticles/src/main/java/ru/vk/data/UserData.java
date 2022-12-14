package ru.vk.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserData implements Serializable {
    private Integer clanId;
    private final int messageTimeout;

    public UserData(Integer clanId, int messageTimeout) {
        this.clanId = clanId;
        this.messageTimeout = messageTimeout;
    }
}
