package ru.vk;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class Book {
    private Long id;
    private String name;
    private Author author;
}

