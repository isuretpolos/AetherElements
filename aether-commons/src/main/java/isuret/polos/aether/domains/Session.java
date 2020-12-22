package isuret.polos.aether.domains;

import org.dizitart.no2.objects.Id;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * The session is a post in a blog
 */
public class Session {

    @Id
    private UUID uuid = UUID.randomUUID();

    private Calendar created = Calendar.getInstance();
    private String intention;
    private String description;
    private List<Paragraph> paragraphs = new ArrayList<>();

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getIntention() {
        return intention;
    }

    public void setIntention(String intention) {
        this.intention = intention;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }
}
