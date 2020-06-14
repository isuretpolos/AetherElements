package isuret.polos.aether.database;

import org.junit.Test;

import java.io.File;

public class DatabaseTest {

    @Test
    public void testBasics() {

        Database database = new Database(new File("target"));
    }

}
