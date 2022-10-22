package ru.vk.shevtsov;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class Book {
    private Long id;
    private String name;
    private Author author;
    private Integer firstPublishedYear;
    private String language;
}
