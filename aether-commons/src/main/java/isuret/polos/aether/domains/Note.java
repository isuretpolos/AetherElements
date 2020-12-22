package isuret.polos.aether.domains;

import java.util.Calendar;

/**
 * A note is a paragraph in a post
 */
public class Note {

    private Calendar created = Calendar.getInstance();
    private String text;

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
