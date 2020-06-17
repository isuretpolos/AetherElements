package isuret.polos.aether.trng;

import isuret.polos.aether.database.Database;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class HotbitsHandlerTest {

    @Test
    public void testRandomness() {

        HotbitsHandler hotbitsHandler = new HotbitsHandler(new Database(new File("target/")));

        for (int x=0; x<10; x++) {
            Assert.assertNotNull(hotbitsHandler.nextInteger());
            Assert.assertNotNull(hotbitsHandler.nextInteger(1000));
            Assert.assertNotNull(hotbitsHandler.nextInteger(1,1000));
            Assert.assertNotNull(hotbitsHandler.nextBoolean());
        }
    }
}
