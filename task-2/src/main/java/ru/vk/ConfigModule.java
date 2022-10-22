package ru.vk;

import com.google.inject.AbstractModule;

public final class ConfigModule extends AbstractModule {
    private final String[] args;

    public ConfigModule(String[] args) {
        this.args = args;
    }

    @Override
    protected void configure() {
        try {
            switch (args[0]) {
                case "console": {
                    bind(Application.class).toInstance(new ConsoleLogApplication(args[1]));
                    break;
                }
                case "file": {
                    bind(Application.class).toInstance(new FileLogApplication(args[1]));
                    break;
                }
                case "combined": {
                    bind(Application.class).toInstance(new CombinedLogApplication(args[1]));
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Wrong logging type, use one of the following: console, file, combined");
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("No tag or logging type defined in program arguments");
        }
    }
}
