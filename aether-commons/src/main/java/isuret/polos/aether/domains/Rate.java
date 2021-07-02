package isuret.polos.aether.domains;

import org.dizitart.no2.objects.Id;

import java.util.UUID;

/**
 * The structural link is known in radionics as the rate, but it can be a name or a cryptic alphanumeric sequence
 */
public class Rate {

    @Id
    private UUID uuid = UUID.randomUUID();

    /**
     * The energetic value aquired during TRNG / Hotbits analysis under the influence of the observers mind.
     */
    private Integer energeticValue = 0;

    /**
     * Name of the signature or a numeric rate, representing the invisible, immaterial and non-local morphic field.
     */
    private String name;

    /**
     * An url pointing towards a description on the web.
     */
    private String url;

    /**
     * The general vitality in relation to the current vitality of the target.
     */
    private Integer gv = 0;

    /**
     * A recurring of a rate object throughout different sessions has a special value,
     * similar to a repeating pattern or a constitutional remedy in homeopathy.
     */
    private Integer recurring = 0;

    /**
     * Sometimes the quality of the TRNG is already limited by an silent observer,
     * then mark the recurring gv in order to spot such occurrences
     */
    private Integer recurringGeneralVitality = 0;

    public Rate() {}

    /**
     * Make a copy
     */
    public Rate(Rate r) {

        energeticValue = r.energeticValue;
        name = r.name;
        url = r.url;
        recurring = r.recurring;
    }

    public Integer getGv() {
        return gv;
    }

    public void setGv(Integer gv) {
        this.gv = gv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getEnergeticValue() {
        return energeticValue;
    }

    public void setEnergeticValue(Integer energeticValue) {
        this.energeticValue = energeticValue;
    }

    public Integer getRecurring() {
        return recurring;
    }

    public void setRecurring(Integer recurring) {
        this.recurring = recurring;
    }

    public Integer getRecurringGeneralVitality() {
        return recurringGeneralVitality;
    }

    public void setRecurringGeneralVitality(Integer recurringGeneralVitality) {
        this.recurringGeneralVitality = recurringGeneralVitality;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "energeticValue=" + energeticValue +
                ", gv=" + gv +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
