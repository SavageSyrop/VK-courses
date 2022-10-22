package ru.vk;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public final class CombinedLogApplication extends Application {

    private final Logger combinedLogger;

    public CombinedLogApplication(String tag) {
        super(tag);
        try {
            LogManager.getLogManager().readConfiguration(getClass().getResourceAsStream("/combinedLog.properties"));
            combinedLogger = Logger.getLogger("combinedLogger");
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
                lineCounter += 2;
                combinedLogger.info(logLine);
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            combinedLogger.severe(e.getMessage());
        }
    }
}
