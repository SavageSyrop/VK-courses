package ru.vk;


import org.jetbrains.annotations.NotNull;

public abstract class Application {
    protected Long lineCounter = 1L;
    protected final String tag;

    public Application(@NotNull String tag) {
        this.tag = tag;
    }

    abstract void waitForInput();
}
