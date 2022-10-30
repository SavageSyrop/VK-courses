package ru.vk;

import com.google.inject.Inject;

public final class LibraryFactory {
    private BooksFactory booksFactory;

    @Inject
    public LibraryFactory(BooksFactory booksFactory) {
        this.booksFactory = booksFactory;
    }

    public Library library(Integer capacity) {
        return new Library(capacity, booksFactory);
    }
}
