package isuret.polos.aether.shell;

import isuret.polos.aether.analysis.AnalysisService;
import isuret.polos.aether.database.Database;
import isuret.polos.aether.trng.HotbitsHandler;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import java.io.File;

/**
 * Based on text-io from Serban Iordache
 * https://text-io.beryx.org/releases/latest/
 */
public class AetherShell {

    private TextIO textIO;
    private AnalysisService analysisService;
    private HotbitsHandler hotbitsHandler;

    public static void main(String [] args) {
        new AetherShell();
    }

    public AetherShell() {
        textIO = TextIoFactory.getTextIO();
        File rootFolder = new File(".");
        hotbitsHandler = new HotbitsHandler(new Database(rootFolder));
        analysisService = new AnalysisService(hotbitsHandler);
        loop();
    }

    private void loop() {

        textIO.getTextTerminal().setBookmark("title");
        textIO.getTextTerminal().println("=== Aether Shell v1.0 ===");
        textIO.getTextTerminal().println("-------------------------");

        while(true) {

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
            if (command.equals(AETHER_COMMANDS.GENERAL_VITALITY)) {
                checkGeneralVitality();
            }

            textIO.getTextTerminal().moveToLineStart();
        }

        System.exit(0);
    }

    private void broadcast() {
        // TODO
        // broadcast your choice / rate or signature
        // select for how long or how high should the dynamic GV check be
    }

    private void analysis() {
        // TODO
        // choose rate list provided in database (also query through subfolders)
        // analyze based on selected rate list
        // display result
    }

    private void checkGeneralVitality() {
        textIO.getTextTerminal().println(analysisService.checkGeneralVitality().toString());
    }


    private void grounding() {

        for (int i=0; i< 120; i++) {

            char c = (char)(hotbitsHandler.nextInteger(94) + '!');
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
