package isuret.polos.aether.logs;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Why use a self written logger?
 * Because I can!
 */
public class Logger {

    private String className;

    public Logger(Class clazz) {
        className = clazz.getName();
    }

    public void info(String message) {
        addMessage(message, "INFO");
    }

    public void warning(String message) {
        addMessage(message, "WARN");
    }

    public void error(String message) {
        addMessage(message, "ERROR");
    }

    private String getDateTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(Calendar.getInstance().getTime());
    }

    private void addMessage(String message, String type) {
        StringBuilder msg = new StringBuilder(getDateTimeString());
        msg.append(" [");
        msg.append(type);
        msg.append("] (");
        msg.append(getShortenedClassName(className));
        msg.append(") - ");
        msg.append(message);
        LoggerSingleton.getInstance().addMessage(msg.toString());
    }

    private String getShortenedClassName(String className) {

        if (!className.contains(".")) return className;

        StringBuilder str = new StringBuilder();

        String [] parts = className.split("\\.");

        for (int x=0; x<parts.length; x++) {
            if (x < parts.length - 1) {
                str.append(parts[x].substring(0, 1));
                str.append(".");
            } else {
                str.append(parts[x]);
            }
        }

        return str.toString();
    }
}
