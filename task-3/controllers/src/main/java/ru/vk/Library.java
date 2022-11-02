package ru.vk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import lombok.Getter;
import ru.vk.exceptions.EmptyShelfException;
import ru.vk.exceptions.ShelfTooSmallException;

import java.util.*;


public final class Library {

    @Getter
    private Map<Integer, Book> books;

    @Getter
    private SortedSet<Integer> emptyIndexes;

    private final Integer shelfMaxSize;

    @Inject
    public Library(Integer shelfMaxSize, BooksFactory booksFactory) {
        this.shelfMaxSize = shelfMaxSize;
        Collection<Book> booksFromFile = booksFactory.books();
        if (booksFromFile.size() > shelfMaxSize) {
            throw new ShelfTooSmallException(shelfMaxSize, booksFromFile.size());
        }
        this.books = new HashMap<>();
        Integer cellCount = 0;
        for (Book book : booksFromFile) {
            books.put(cellCount, book);
            cellCount++;
        }

        this.emptyIndexes = new TreeSet<>();
        for (int i = books.size(); i< shelfMaxSize; i++) {
            emptyIndexes.add(i);
        }
    }

    public void printBooks() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        System.out.println(gson.toJson(books));
    }

    public Book takeBook(int shellIndex) {
        if (books.get(shellIndex) == null) {
            throw new EmptyShelfException(shellIndex);
        }
        Book book = books.remove(shellIndex);
        emptyIndexes.add(shellIndex);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        System.out.println(gson.toJson(book) + "\n Was at book shell with index " + shellIndex);
        return book;
    }

    public Integer putBook(Book book) {
        if (shelfMaxSize == books.size()) {
            throw new ShelfTooSmallException(shelfMaxSize, shelfMaxSize + 1);
        }
        try {
            Integer nearestIndex = emptyIndexes.first();
            books.put(nearestIndex, book);
            emptyIndexes.remove(nearestIndex);
            return nearestIndex;
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
