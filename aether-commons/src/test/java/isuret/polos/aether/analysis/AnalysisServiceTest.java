package isuret.polos.aether.analysis;

import isuret.polos.aether.database.Database;
import isuret.polos.aether.trng.HotbitsHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class AnalysisServiceTest {

    private AnalysisService analysisService;

    @Before
    public void init() {
        analysisService = new AnalysisService(new HotbitsHandler(new Database(new File("target/"))));
    }

    @After
    public void shutDown() {
        analysisService.shutDown();
    }

    /**
     * finished after 40 attempts for a minimum gv of 500
     * finished after 35 attempts for a minimum gv of 400
     * finished after 215 attempts for a minimum gv of 300
     * finished after 4521 attempts for a minimum gv of 200
     * finished after 45186 attempts for a minimum gv of 100
     * -----
     * finished after 0 attempts for a maximum gv of 1000
     * finished after 13 attempts for a maximum gv of 2000
     * finished after 18 attempts for a maximum gv of 3000
     * finished after 3198 attempts for a maximum gv of 4000
     * finished after 3733 attempts for a maximum gv of 5000
     * finished after 131280 attempts for a maximum gv of 6000
     * ... 7000 never reached after several minutes
     */
    @Test
    public void testGeneralVitality() {

        for (int x=0; x<10; x++) {
            int gv = analysisService.checkGeneralVitality();
            System.out.println(gv);
        }

        checkGvForMinimumValue(500);
        checkGvForMinimumValue(400);
        checkGvForMinimumValue(300);
        //checkGvForMinimumValue(200);
        //checkGvForMinimumValue(100);

        checkGvForMaximumValue(1000);
        checkGvForMaximumValue(2000);
        checkGvForMaximumValue(3000);
        //checkGvForMaximumValue(4000);
        //checkGvForMaximumValue(5000);
        //checkGvForMaximumValue(6000);
        //checkGvForMaximumValue(7000);
    }

    private void checkGvForMinimumValue(Integer minimum) {
        int count = 0;

        while (analysisService.checkGeneralVitality() > minimum) {
            count++;
        }

        System.out.println("finished after " + count + " attempts for a minimum gv of " + minimum);
    }

    private void checkGvForMaximumValue(Integer maximum) {
        int count = 0;

        while (analysisService.checkGeneralVitality() < maximum) {
            count++;
        }

        System.out.println("finished after " + count + " attempts for a maximum gv of " + maximum);
    }
}
