package ru.vk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public final class PlusOneFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        List<String> text = new ArrayList<>(List.of(record.getMessage().split(" ")));
        Integer lineNum = Integer.parseInt(text.get(0)) + 1;
        text.remove(0);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss yyyy-MM-dd");
        String result = dateFormat.format(record.getMillis()) + " " + record.getLevel().getName() + " "  + record.getSourceClassName() +  " " + record.getSourceMethodName() + ": " + lineNum + " " + String.join(" ", text) + "\n";
        return result;
    }
}
