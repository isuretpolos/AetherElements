package isuret.polos.aether.database;

import isuret.polos.aether.domains.HotBitIntegers;
import isuret.polos.aether.domains.Settings;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class DatabaseTest {

    private Database database = new Database(new File("target/"));

    @Test
    public void testBasics() throws IOException {

        Settings settings = new Settings();
        settings.getProperties().put("testkey","123");
        settings.getProperties().put("boolean",true);
        settings.getProperties().put("int",55);

        database.saveSettings(settings);

        settings = database.getSettings();

        Assert.assertEquals("123", settings.getProperties().get("testkey"));
        Assert.assertEquals(true, settings.getProperties().get("boolean"));
        Assert.assertEquals(55, settings.getProperties().get("int"));
    }

    @Test
    public void testHotbits() throws IOException {

        HotBitIntegers hotBitIntegers = new HotBitIntegers();

        for (int x=0; x<10; x++) {
            hotBitIntegers.getIntegerList().add(x);
        }

        database.save("hotbits/hotbits_test.json",hotBitIntegers);

        hotBitIntegers = database.getHotBitPackage();

        Assert.assertNotNull(hotBitIntegers);
    }

}
