package isuret.polos.aether.database;

import isuret.polos.aether.analysis.AnalysisService;
import isuret.polos.aether.domains.*;
import isuret.polos.aether.trng.HotbitsHandler;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class DatabaseTest {

    private Database database;

    @Before
    public void init() throws IOException {
        database = new Database(new File("target/"));
        FileUtils.copyFile(new File("src/test/resources/HOMEOPATHY_Clarke_With_MateriaMedicaUrls.txt"),
                new File("target/database/rates/HOMEOPATHY_Clarke_With_MateriaMedicaUrls.txt"));
    }

    @After
    public void shutDown() {
        database.shutDown();
    }

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

    @Test
    public void testSessionCRUD() {

        User user = new User();
        user.setUsername("John The Tester");
        user.setPassword("12345");

        AnalysisService analysisService = new AnalysisService(new HotbitsHandler(database));

        Case myCase = new Case();
        myCase.setName("Test area Nevada");
        myCase.setDescription("A test");
        myCase.setLastChange(Calendar.getInstance());

        Session session = new Session();
        session.setIntention("Analyse the energy");

        Integer gv = analysisService.checkGeneralVitality();
        AnalysisResult result = analysisService.analyze("HOMEOPATHY_Clarke_With_MateriaMedicaUrls.txt");
        System.out.println(result);

        Note note = new Note();
        note.setText("This is a test note");
        session.getParagraphs().add(new Paragraph(note));
        session.getParagraphs().add(new Paragraph(result));
        myCase.getSessionList().add(session);

        database.saveCase(user, myCase);
        Case caseFromDB = database.readCase(user, myCase.getName());

        System.out.println(caseFromDB);

        user.setUsername("Another John");
        user.setPassword("987654321");

        database.saveCase(user, myCase);
        caseFromDB = database.readCase(user, myCase.getName());

        Assert.assertNotNull(caseFromDB);
        Assert.assertEquals(1,caseFromDB.getSessionList().size());
        Assert.assertEquals(2,caseFromDB.getSessionList().get(0).getParagraphs().size());
    }
}
