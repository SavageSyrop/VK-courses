package ru.vk.exceptions;

public class ShelfTooSmallException extends RuntimeException {
    public ShelfTooSmallException(Integer shellSize, Integer booksCount) {
        super("Shelf is too small. Shelf size: " + shellSize + ". Received " + booksCount + " books.");
    }
}
