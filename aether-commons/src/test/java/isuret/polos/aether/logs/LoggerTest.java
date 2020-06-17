package isuret.polos.aether.logs;

import org.junit.Assert;
import org.junit.Test;

public class LoggerTest {

    private Logger logger = new Logger(LoggerTest.class);

    @Test
    public void test() {
        logger.info("TEST");

        testLoggerContent();

        for (int x=0; x< 110; x++) {
            logger.info("TEST " + x);
            logger.warning("WARNING " + x);
            logger.error("ERROR " + x);
        }

        testLoggerContent();
    }

    private void testLoggerContent() {

        Assert.assertTrue(LoggerSingleton.getInstance().getMessages().size() < 101);

        for (String str : LoggerSingleton.getInstance().getMessages()) {
            Assert.assertNotNull(str);
            Assert.assertTrue(str.length() > 10);
        }
    }
}
