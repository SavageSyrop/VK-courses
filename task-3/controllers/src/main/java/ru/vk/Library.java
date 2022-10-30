package ru.vk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import lombok.Getter;
import ru.vk.exceptions.EmptyShellException;
import ru.vk.exceptions.ShellTooSmallException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public final class Library {

    private final BooksFactory booksFactory;

    @Getter
    private Map<Integer, Book> books;

    private final Integer shellMaxSize;

    @Inject
    public Library(Integer shellMaxSize, BooksFactory booksFactory) {
        this.shellMaxSize = shellMaxSize;
        this.booksFactory = booksFactory;
        Collection<Book> booksFromFile = booksFactory.books();
        if (booksFromFile.size() > shellMaxSize) {
            throw new ShellTooSmallException(shellMaxSize, booksFromFile.size());
        }
        this.books = new HashMap<>();
        Integer cellCount = 0;
        for (Book book : booksFromFile) {
            books.put(cellCount, book);
            cellCount++;
        }
    }

    public void printBooks() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        System.out.println(gson.toJson(books));
    }

    public Book takeBook(int shellIndex) {
        if (books.get(shellIndex) == null) {
            throw new EmptyShellException(shellIndex);
        }
        Book book = books.remove(shellIndex);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        System.out.println(gson.toJson(book) + "\n Was at book shell with index " + shellIndex);
        return book;
    }

    public Integer putBook(Book book) {
        if (shellMaxSize == books.size()) {
            throw new ShellTooSmallException(shellMaxSize, shellMaxSize + 1);
        }

        for (Integer i = 0; i < shellMaxSize; i++) {
            if (books.get(i) == null) {
                books.put(i, book);
                return i;
            }
        }
        return null;
    }
}
