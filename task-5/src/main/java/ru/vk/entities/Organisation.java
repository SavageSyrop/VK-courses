package ru.vk.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Organisation {
    private Long taxNumber;
    private String name;
    private Long checkingAccount;
}
