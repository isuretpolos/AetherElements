package isuret.polos.aether.analysis;

import isuret.polos.aether.trng.HotbitsHandler;

public class AnalysisService {

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
}
