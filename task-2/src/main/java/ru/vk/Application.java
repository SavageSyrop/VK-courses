package ru.vk;


import com.google.inject.Inject;

import java.util.NoSuchElementException;
import java.util.Scanner;

public  class Application {
    private final CustomLogger logger;

    @Inject
    public Application(CustomLogger logger) {
        this.logger = logger;
    }

    void waitForInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Waiting for new lines. Key in Ctrl+D to exit.");
            while (true) {
                String line = scanner.nextLine();
                logger.info(line);
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }
}
