package ru.vk;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class ConsoleLogApplication extends Application {

    private final Logger consoleLogger;

    public ConsoleLogApplication(String tag) {
        super(tag);
        try {
            LogManager.getLogManager().readConfiguration(getClass().getResourceAsStream("/consoleLog.properties"));
            consoleLogger = Logger.getLogger("consoleLogger");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void waitForInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Waiting for new lines. Key in Ctrl+D to exit.");
            while (true) {
                String line = scanner.nextLine();
                String logLine = lineCounter + " <" + tag + ">" + line + "</" + tag + ">";
                lineCounter++;
                consoleLogger.info(logLine);
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }
}
