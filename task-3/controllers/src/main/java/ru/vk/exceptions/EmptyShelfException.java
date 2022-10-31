package ru.vk.exceptions;

public class EmptyShelfException extends RuntimeException {
    public EmptyShelfException(Integer emptyShellIndex) {
        super("Shelf with index " + emptyShellIndex + " is empty");
    }
}
