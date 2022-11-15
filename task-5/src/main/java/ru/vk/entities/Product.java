package ru.vk.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Product {
    private Long code;
    private String name;

    public Product(String name) {
        this.code = null;
        this.name = name;
    }
}
