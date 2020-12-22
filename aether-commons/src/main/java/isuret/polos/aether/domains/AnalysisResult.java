package isuret.polos.aether.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * The radionics analysis result
 */
public class AnalysisResult {

    private Calendar dateTime = Calendar.getInstance();
    private Integer generalVitality;
    private List<Rate> rateList = new ArrayList<>();

    public List<Rate> getRateList() {
        return rateList;
    }

    public Calendar getDateTime() {
        return dateTime;
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
                "dateTime=" + dateTime +
                ", generalVitality=" + generalVitality +
                ", rateList=" + rateList +
                '}';
    }
}
