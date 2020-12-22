package isuret.polos.aether.domains;

import org.dizitart.no2.objects.Id;

import java.util.Calendar;
import java.util.UUID;

/**
 * Broadcast data and protocol (before and after)
 */
public class BroadCastData {

    @Id
    private UUID uuid = UUID.randomUUID();
    private Calendar created = Calendar.getInstance();
    private Boolean clear = false;
    private String intention;
    private String signature;
    private Integer delay = 25;
    private Integer repeat = 1;
    private Integer enteringWithGeneralVitality;
    private Integer leavingWithGeneralVitality;

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

    public Boolean getClear() {
        return clear;
    }

    public void setClear(Boolean clear) {
        this.clear = clear;
    }

    public String getIntention() {
        return intention;
    }

    public void setIntention(String intention) {
        this.intention = intention;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Integer getRepeat() {
        return repeat;
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    public Integer getEnteringWithGeneralVitality() {
        return enteringWithGeneralVitality;
    }

    public void setEnteringWithGeneralVitality(Integer enteringWithGeneralVitality) {
        this.enteringWithGeneralVitality = enteringWithGeneralVitality;
    }

    public Integer getLeavingWithGeneralVitality() {
        return leavingWithGeneralVitality;
    }

    public void setLeavingWithGeneralVitality(Integer leavingWithGeneralVitality) {
        this.leavingWithGeneralVitality = leavingWithGeneralVitality;
    }
}
