package ru.vk.shevtsov;


import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
public class Library {
    private HashMap<String, List<Book>> books;

    public List<Book> getBooksByAuthor(String authorName) {
        return books.get(authorName);
    }
}
