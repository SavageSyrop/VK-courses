package ru.vk;

import com.google.inject.AbstractModule;

public final class ConfigModule extends AbstractModule {
    private final String[] args;

    public ConfigModule(String[] args) {
        this.args = args;
    }

    @Override
    protected void configure() {

        if (args.length < 2) {
            throw new IllegalArgumentException("Provide logging type and tag in program arguments! Example: console flag");
        }

        bind(CustomLogger.class).toInstance(LoggerTypeFactotry.valueOf(args[0]).logger(args[1]));
    }
}
