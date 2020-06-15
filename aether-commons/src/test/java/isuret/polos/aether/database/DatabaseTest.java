package isuret.polos.aether.database;

import isuret.polos.aether.domains.Settings;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class DatabaseTest {

    @Test
    public void testBasics() throws IOException {

        Database database = new Database(new File("target"));

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

}
