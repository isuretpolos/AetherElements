package isuret.polos.aether.shell;

import isuret.polos.aether.analysis.AnalysisService;
import isuret.polos.aether.database.Database;
import isuret.polos.aether.domains.AnalysisResult;
import isuret.polos.aether.domains.Rate;
import isuret.polos.aether.trng.HotbitsHandler;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import java.io.File;
import java.util.List;

/**
 * Based on text-io from Serban Iordache
 * https://text-io.beryx.org/releases/latest/
 */
public class AetherShell {

    private TextIO textIO;
    private AnalysisService analysisService;
    private HotbitsHandler hotbitsHandler;
    private Database database;
    private AnalysisResult lastResult;

    public static void main(String[] args) {
        new AetherShell();
    }

    public AetherShell() {
        textIO = TextIoFactory.getTextIO();
        File rootFolder = new File(".");
        database = new Database(rootFolder);
        hotbitsHandler = new HotbitsHandler(database);
        analysisService = new AnalysisService(hotbitsHandler);
        loop();
    }

    private void loop() {

        textIO.getTextTerminal().setBookmark("title");
        textIO.getTextTerminal().println("=== Aether Shell v1.0 ===");
        textIO.getTextTerminal().println("-------------------------");

        while (true) {

            AETHER_COMMANDS command = textIO.newEnumInputReader(AETHER_COMMANDS.class).read("");
            textIO.getTextTerminal().println("Your choice was " + command.name() + "\n");

            if (command.equals(AETHER_COMMANDS.EXIT)) break;
            if (command.equals(AETHER_COMMANDS.GROUNDING) || command.equals(AETHER_COMMANDS.CLEARING)) {
                grounding();
            }
            if (command.equals(AETHER_COMMANDS.ANALYSIS)) {
                analysis();
            }
            if (command.equals(AETHER_COMMANDS.BROADCAST)) {
                broadcast();
            }
            if (command.equals(AETHER_COMMANDS.CLEARING)) {
                clearing();
            }
            if (command.equals(AETHER_COMMANDS.GENERAL_VITALITY)) {
                checkGeneralVitality();
            }

            textIO.getTextTerminal().moveToLineStart();
        }

        hotbitsHandler.shutDown();

        System.exit(0);
    }

    private void clearing() {
        broadcast("UV LIGHT CLEAR");
    }

    private void broadcast() {

        String menuChoices = "(1) TYPE IN YOUR OWN RATE OR SIGNATURE";

        if (lastResult != null) {
            menuChoices += "\n(2) SELECT RATE FROM LAST ANALYSIS RESULT";
        }

        Integer choice = textIO.newIntInputReader().read(menuChoices);
        String rate = "";

        if (choice == 2) {
            showResult(lastResult);
            Integer rateChoice = textIO.newIntInputReader().read("SELECT RATE FOR BROADCASTING");
            rate = lastResult.getRateList().get(rateChoice - 1).getName();
        } else {
            rate = textIO.newStringInputReader().read("TYPE IN YOUR INTENTION");
        }

        textIO.getTextTerminal().println("BROADCASTING: " + rate);

        broadcast(rate);
        // TODO select for how long or how high should the dynamic GV check be
    }

    private void analysis() {

        int i = 1;
        List<String> rateList = database.getRatesFromPath("database/rates/");

        for (String rate : rateList) {
            textIO.getTextTerminal().println(i + " - " + rate);
            i++;
        }

        Integer rateChoice = textIO.newIntInputReader().read("SELECT RATE LIST");
        String rateListName = rateList.get(rateChoice - 1);
        textIO.getTextTerminal().println("RATE SELECTED: " + rateListName);

        AnalysisResult result = analysisService.analyze(rateListName);

        showResult(result);

        lastResult = result;
    }

    private void showResult(AnalysisResult result) {
        for (int x = 0; x< result.getRateList().size(); x++) {
            if (x >= 10) break;

            Rate rate = result.getRateList().get(x);
            textIO.getTextTerminal().println("(" + (x + 1) + ")  " + rate.getEnergeticValue() + " - " + rate.getName());
        }
    }

    private void checkGeneralVitality() {
        textIO.getTextTerminal().println(analysisService.checkGeneralVitality().toString());
    }


    private void grounding() {

        broadcast("grounding earth core root deep breath");
    }

    private void broadcast(String intention) {

        intention = intention.replaceAll(" ","").trim();

        for (int i = 0; i < 120; i++) {

            char c = 0;

            if (hotbitsHandler.nextInteger(100) > 50) {
                c = intention.toCharArray()[hotbitsHandler.nextInteger(intention.length())];
            } else {
                c = (char) (hotbitsHandler.nextInteger(94) + '!');
            }

            textIO.getTextTerminal().print(Character.toString(c));
            delay();
        }

        textIO.getTextTerminal().println();
    }

    private void delay() {
        delay(200);
    }

    private void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
