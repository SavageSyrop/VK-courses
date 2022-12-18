package ru.vk.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClanData implements Serializable {
    private int id;
    private Integer capacityUser;
    private Integer capacityModerator;
    private List<Integer> memberUser;
    private List<Integer> memberModerator;
    private Integer adminId;


    public ClanData() {
        this.capacityUser = null;
        this.capacityModerator = null;
        this.memberUser = new ArrayList<>();
        this.memberModerator = new ArrayList<>();
        this.adminId = null;
    }
}
