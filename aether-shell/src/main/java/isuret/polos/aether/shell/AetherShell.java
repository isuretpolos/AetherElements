package isuret.polos.aether.shell;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

/**
 * Based on text-io from Serban Iordache
 * https://text-io.beryx.org/releases/latest/
 */
public class AetherShell {

    private TextIO textIO;

    public static void main(String [] args) {
        new AetherShell();
    }

    public AetherShell() {
        textIO = TextIoFactory.getTextIO();
        loop();
    }

    private void loop() {
        textIO.getTextTerminal().setBookmark("title");
        textIO.getTextTerminal().println("=== Aether Shell v1.0 ===");
        textIO.getTextTerminal().println("-------------------------");

        while(true) {

            AETHER_COMMANDS command = textIO.newEnumInputReader(AETHER_COMMANDS.class).read("");
            textIO.getTextTerminal().println("Your choice was " + command.name());

            if (command.equals(AETHER_COMMANDS.EXIT)) break;
            if (command.equals(AETHER_COMMANDS.GROUNDING)) {
                grounding();
            }

            //textIO.getTextTerminal().resetToBookmark("title");
        }

        System.exit(0);
    }


    private void grounding() {
        textIO.getTextTerminal().setBookmark("title");

        for (int i=0; i< 40; i++) {
            textIO.getTextTerminal().print("#");
            delay();
        }

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
