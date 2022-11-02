package ru.vk;

import lombok.AccessLevel;
import lombok.Getter;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Getter(AccessLevel.PROTECTED)
public class CustomLogger {
    private final String tag;
    private final Logger logger;
    private final int increase;
    private Long lineCounter = 1L;


    public CustomLogger(String tag, String loggerName, String propertyFile, int increase) {
        try {
            LogManager.getLogManager().readConfiguration(getClass().getResourceAsStream(propertyFile));
            logger = Logger.getLogger(loggerName);
            this.increase = increase;
            this.tag = tag;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void info(String line) {
        String logLine = lineCounter + " <" + tag + ">" + line + "</" + tag + ">";
        lineCounter+=increase;
        logger.info(logLine);
    }
}
