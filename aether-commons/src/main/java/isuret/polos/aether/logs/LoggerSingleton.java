package isuret.polos.aether.logs;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton holding the messages from the logs inside the memory
 */
public class LoggerSingleton {

    private boolean sysOutMessages = false;
    private Integer maxLines = 100;
    private List<String> messages = new ArrayList<>();
    private static LoggerSingleton loggerSingleton;

    private LoggerSingleton() {}

    public static LoggerSingleton getInstance() {

        if (loggerSingleton == null) {
            loggerSingleton = new LoggerSingleton();
        }

        return loggerSingleton;
    }

    public static void setMaxLines(int maxLines) {
        getInstance().setMaxLinesIntern(maxLines);
    }

    private void setMaxLinesIntern(int maxLines) {
        this.maxLines = maxLines;
    }

    public static void setSysOutMessages(boolean show) {
        getInstance().setSysOutMessagesIntern(show);
    }

    private void setSysOutMessagesIntern(boolean show) {
        this.sysOutMessages = show;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message) {

        if (sysOutMessages) {
            System.out.println(message);
        }

        messages.add(message);

        if (messages.size() > maxLines) {
            messages.remove(0);
        }
    }
}
