package isuret.polos.aether.shell;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

public class AetherShell {

    public static void main(String [] args) {
        TextIO textIO = TextIoFactory.getTextIO();

        textIO.getTextTerminal().print("Aether Shell v1.0");

        while(true) {
            AETHER_COMMANDS command = textIO.newEnumInputReader(AETHER_COMMANDS.class).read("What do you want to do?");

            textIO.getTextTerminal().print("Your choice was " + command.name() + "\n");

            if (command.equals(AETHER_COMMANDS.EXIT)) break;
        }

        System.exit(0);
    }
}
