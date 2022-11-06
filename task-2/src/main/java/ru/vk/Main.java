package ru.vk;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jetbrains.annotations.NotNull;

public class Main {
    public static void main(@NotNull String[] args) {
        final Injector injector = Guice.createInjector(new ConfigModule(args));
        injector.getInstance(Application.class).waitForInput();
    }
}