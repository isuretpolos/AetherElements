package isuret.polos.aether.domains;

import java.util.ArrayList;
import java.util.List;

/**
 * The central element of digital radionics are the hotbits
 * <p><a href="https://www.fourmilab.ch/hotbits/">Genuine random numbers</a>,
 * only that these are not from radioactive decay, but from electric noise generators (diode, webcam)</p>
 */
public class HotBitIntegers {

    private List<Integer> integerList = new ArrayList<>();

    public List<Integer> getIntegerList() {
        return integerList;
    }

    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }
}
