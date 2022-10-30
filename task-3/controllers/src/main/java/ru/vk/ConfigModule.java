package ru.vk;

import com.google.inject.AbstractModule;

public final class ConfigModule extends AbstractModule {
    private final String pathToFile;

    public ConfigModule(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    @Override
    protected void configure() {
        bind(BooksFactory.class).toInstance(new FileBookFactory(pathToFile));
    }
}
