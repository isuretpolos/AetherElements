package isuret.polos.aether.domains;

import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * A case represents, well a case (or a blog)
 * ... a target, person, area, thing, abstract thought or whatever a radionic practitioner analysize and balance
 */
@Indices({
        @Index(value = "name", type = IndexType.Unique)
})
public class Case {

    @Id
    private UUID uuid = UUID.randomUUID();

    /**
     * The name of the target, person or area (animal or what else you need to observe)
     */
    private String name;

    /**
     * Describe the reason why you take the case and what the main problem is
     */
    private String description;
    private Calendar created = Calendar.getInstance();
    private Calendar lastChange;

    /**
     * A list of sessions / actions
     */
    private List<Session> sessionList = new ArrayList<>();

    /**
     * Top Ten of rates determined by analysis (Statistics)
     */
    private List<Rate> topTenList = new ArrayList<>();

    public Case() {
        created = Calendar.getInstance();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Calendar getLastChange() {
        return lastChange;
    }

    public void setLastChange(Calendar lastChange) {
        this.lastChange = lastChange;
    }

    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<Session> sessionList) {
        this.sessionList = sessionList;
    }

    public List<Rate> getTopTenList() {
        return topTenList;
    }

    public void setTopTenList(List<Rate> topTenList) {
        this.topTenList = topTenList;
    }

    @Override
    public String toString() {
        return "Case{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", created=" + created +
                ", lastChange=" + lastChange +
                ", topTenList=" + topTenList +
                '}';
    }
}
