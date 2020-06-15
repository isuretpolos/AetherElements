package isuret.polos.aether.trng;

import com.fasterxml.jackson.databind.ObjectMapper;
import isuret.polos.aether.database.Database;
import isuret.polos.aether.domains.HotBitIntegers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HotbitsHandler {

    private Database database;
    private List<Integer> hotbits = new ArrayList<Integer>();

    public HotbitsHandler(Database database) {
        this.database = database;
    }

    private synchronized void loadHotbitsFromHarddisk() {

        if (hotbits.size() > 4000000) return;

        HotBitIntegers integers = database.getHotBitPackage();

        for (Integer number : integers.getIntegerList()) {
            hotbits.add(number);
        }

        if (hotbits.size() > 40000) return;
    }
}
