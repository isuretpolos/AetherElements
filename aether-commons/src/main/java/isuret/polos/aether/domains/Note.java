package isuret.polos.aether.domains;

import org.dizitart.no2.objects.Id;

import java.util.Calendar;
import java.util.UUID;

/**
 * A note is a paragraph in a post (or session)
 */
public class Note {

    @Id
    private UUID uuid = UUID.randomUUID();

    private Calendar created = Calendar.getInstance();
    private String text;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

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
