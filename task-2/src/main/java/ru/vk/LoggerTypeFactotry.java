package ru.vk;

import org.jetbrains.annotations.NotNull;

enum LoggerTypeFactotry {
    Console {
        @NotNull
        @Override
        public CustomLogger logger(@NotNull String tag) {
            return new CustomLogger(tag, "consoleLogger", "/consoleLog.properties", 1);
        }
    },
    File {
        @NotNull
        @Override
        public CustomLogger logger(@NotNull String tag) {
            return new CustomLogger(tag, "fileLogger", "/fileLog.properties", 1);
        }
    },
    Combined {
        @NotNull
        @Override
        public CustomLogger logger(@NotNull String tag) {
            return new CustomLogger(tag, "combinedLogger", "/combinedLog.properties", 2);
        }
    };

    public abstract @NotNull CustomLogger logger(@NotNull String tag);
}