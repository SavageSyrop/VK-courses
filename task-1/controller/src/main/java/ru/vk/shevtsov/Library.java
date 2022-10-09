package ru.vk.shevtsov;


import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
public class Library {
    private HashMap<Long, Book> books;

    public List<Book> getBooksByAuthor(String authorName) {
        List<Book> foundBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthor().getName().equals(authorName)) {
                foundBooks.add(book);
            }
        }
        return foundBooks;
    }
}
