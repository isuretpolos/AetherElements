package isuret.polos.aether.trng;

import isuret.polos.aether.database.Database;
import isuret.polos.aether.domains.HotBitIntegers;
import isuret.polos.aether.logs.Logger;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * The main hotbits handler delivers randomness for your PSI experiments or radionics analysis
 */
public class HotbitsHandler {

    private Logger logger = new Logger(HotBitIntegers.class);
    private Database database;
    private List<Integer> hotbits = new ArrayList<Integer>();
    private SecureRandom secureRandom = new SecureRandom();
    private Boolean simulationMode = true;

    public HotbitsHandler(Database database) {
        this.database = database;
        secureRandom.setSeed(Calendar.getInstance().getTimeInMillis());
    }

    /**
     * This is called whenever you try to get a random value (it refills its pool of randomness)
     * @throws IOException
     */
    private synchronized void loadHotbitsFromHarddisk() throws IOException {

        if (hotbits.size() > 10000) return;

        HotBitIntegers integers = database.getHotBitPackage();

        if (integers == null) return;

        for (Integer number : integers.getIntegerList()) {
            hotbits.add(number);
        }
    }

    /**
     * Get the next random integer
     * @return integer
     */
    public Integer nextInteger() {

        return getRandom().nextInt();
    }

    /**
     * Get the next random integer till max value
     * @return integer
     */
    public Integer nextInteger(int max) {
        return getRandom().nextInt(max);
    }

    /**
     * Get the next random integer between min and max
     * @return integer
     */
    public Integer nextInteger(int min, int max) {
        return getRandom().nextInt(max-min) + min;
    }

    /**
     * Get the next random bit
     * @return boolean
     */
    public Boolean nextBoolean() {

        return getRandom().nextBoolean();
    }

    /**
     * Returns a hotbits seeded Random object or the SecureRandom which simulates the randomness (or even is partial
     * non-deterministic, if you run it on a RaspberryPi with /dev/random configured ... if you are a linux pro)
     * @return
     */
    private Random getRandom() {

        try {

            loadHotbitsFromHarddisk();

            if (hotbits.size() > 0) {
                simulationMode = false;
                return new Random(hotbits.remove(0));
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        simulationMode = true;
        return secureRandom;
    }

    /**
     * Returns the size of hotbits in the list / array ... you can use this for displaying it in a window for example
     * @return
     */
    public Integer getHotbitsSize() {
        return hotbits.size();
    }

    /**
     * If no hotbits (true random numbers from a natural source) are available, the HotbitsHandler switch to a
     * "simulation mode", which means it use SecureRandom with partial true randomness (depends on your operating system)
     * @return true if simulation mode (no hotbits seeded randomness)
     */
    public Boolean getSimulationMode() {
        return simulationMode;
    }
}
