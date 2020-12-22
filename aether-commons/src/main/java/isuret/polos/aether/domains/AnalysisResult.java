package isuret.polos.aether.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dizitart.no2.objects.Id;

import java.util.*;

/**
 * The radionics analysis result
 */
public class AnalysisResult {

    @Id
    private UUID uuid = UUID.randomUUID();
    private Calendar created = Calendar.getInstance();
    private Integer generalVitality;
    private List<Rate> rateList = new ArrayList<>();

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<Rate> getRateList() {
        return rateList;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public void setRateList(List<Rate> rateList) {
        this.rateList = rateList;
    }

    public Integer getGeneralVitality() {
        return generalVitality;
    }

    public void setGeneralVitality(Integer generalVitality) {
        this.generalVitality = generalVitality;
    }

    @JsonIgnore
    public AnalysisResult sort() {
        Collections.sort(rateList, new Comparator<Rate>() {
            @Override
            public int compare(Rate o1, Rate o2) {

                if (o2.getEnergeticValue().equals(o1.getEnergeticValue())) {
                    return o1.getName().compareTo(o2.getName());
                }

                return o2.getEnergeticValue().compareTo(o1.getEnergeticValue());
            }
        });
        return this;
    }

    @JsonIgnore
    public AnalysisResult shorten(Integer maxSize) {

        while (rateList.size() > maxSize) {
            rateList.remove(rateList.size() - 1);
        }

        return this;
    }

    @Override
    public String toString() {
        return "AnalysisResult{" +
                "dateTime=" + created +
                ", generalVitality=" + generalVitality +
                ", rateList=" + rateList +
                '}';
    }
}
