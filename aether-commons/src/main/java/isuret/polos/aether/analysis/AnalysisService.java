package isuret.polos.aether.analysis;

import isuret.polos.aether.domains.AnalysisResult;
import isuret.polos.aether.domains.Rate;
import isuret.polos.aether.trng.HotbitsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisService {

    // TODO get all these values from the settings
    public static final int MAX_RATELIST_SIZE = 5000;
    private Integer maxValue = 100;
    public final static int MAX_ENTRIES = 21;
    //----
    private HotbitsHandler hotbitsHandler;

    public AnalysisService(HotbitsHandler hotbitsHandler) {
        this.hotbitsHandler = hotbitsHandler;
    }

    public Integer checkGeneralVitality() {

        Integer gv = 0;

        for (int x=0; x<5; x++) {
            int randomNumber = hotbitsHandler.nextInteger(1000);
            if (gv < randomNumber) gv = randomNumber;
        }

        if (gv > 950) {
            Integer randomNumber;
            while((randomNumber = hotbitsHandler.nextInteger(100)) > 10) {
                gv += randomNumber;
            }
        }

        return gv;
    }

    public AnalysisResult analyze(String rateListName) {

        List<Rate> rateList = hotbitsHandler.getDatabase().getRates(rateListName);

        AnalysisResult analysisResult = new AnalysisResult();

        // First shuffle the rate list
        List<Rate> shuffleRateList = shuffleRateList(rateList);

        Map<String, Rate> rates = new HashMap<>();

        int max = shuffleRateList.size() / 10;
        if (max > MAX_RATELIST_SIZE) max = MAX_RATELIST_SIZE;
        int count = 0;
        int biggestLevel = 0;
        boolean analysisFinished = false;

        /**
         * Get some rates (till max count depending on the rate list size)
         */
        while (shuffleRateList.size() > 0) {

            int x = hotbitsHandler.nextInteger(0, shuffleRateList.size() - 1);
            Rate rate = shuffleRateList.remove(x);
            rates.put(rate.getName(), rate);

            count += 1;

            if (count >= max) {
                break;
            }
        }

        /**
         * Add energetic value
         */
        while (!analysisFinished) {
            for (Rate rate : rates.values()) {

                Integer energeticValue = rate.getEnergeticValue();

                energeticValue += hotbitsHandler.nextInteger(10);

                rate.setEnergeticValue(energeticValue);
                rates.put(rate.getName(), rate);

                if (energeticValue > biggestLevel) {
                    biggestLevel = energeticValue;
                }

                if (biggestLevel >= maxValue) {
                    analysisFinished = true;
                    break;
                }
            }
        }

        analysisResult.getRateList().addAll(rates.values());
        AnalysisResult sortedResult = analysisResult.sort().shorten(MAX_ENTRIES);

        return sortedResult;
    }

    /**
     * The cards are remixed
     * @param rateList
     * @return
     */
    private List<Rate> shuffleRateList(List<Rate> rateList) {

        List<Rate> shuffledRateList = new ArrayList<>();

        while (rateList.size() > 1) {
            int position = hotbitsHandler.nextInteger(0, rateList.size() - 1);
            shuffledRateList.add(rateList.remove(position));
        }

        if (rateList.size() == 1) shuffledRateList.add(rateList.get(0));

        return shuffledRateList;
    }

    public void shutDown() {
        hotbitsHandler.shutDown();
    }

}
