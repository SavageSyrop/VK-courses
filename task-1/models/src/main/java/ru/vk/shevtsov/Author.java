package ru.vk.shevtsov;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
public class Author {
    private Long id;
    private String name;
    private LocalDate bornDate;
    private LocalDate deathDate;
}
