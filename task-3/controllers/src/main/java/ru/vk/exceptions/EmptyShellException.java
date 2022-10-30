package ru.vk.exceptions;

public class EmptyShellException extends RuntimeException {
    public EmptyShellException(Integer emptyShellIndex) {
        super("Shell with index " + emptyShellIndex + " is empty");
    }
}
