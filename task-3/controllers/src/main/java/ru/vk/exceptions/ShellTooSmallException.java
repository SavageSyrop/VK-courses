package ru.vk.exceptions;

public class ShellTooSmallException extends RuntimeException {
    public ShellTooSmallException(Integer shellSize, Integer booksCount) {
        super("Shell is too small. Shell size: " + shellSize + ". Received " + booksCount + " books.");
    }
}
