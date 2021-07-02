package isuret.polos.aether.photography;

import isuret.polos.aether.database.Database;
import isuret.polos.aether.trng.HotbitsHandler;
import processing.core.PApplet;

import java.io.File;
import java.util.Random;

public class ProofOfConcept extends PApplet {

    private HotbitsHandler hotbitsHandler = new HotbitsHandler(new Database(new File("../")));
    private Random random = new Random();

    public static void main(String[] args) {
        ProofOfConcept.main(ProofOfConcept.class.getName());
    }

    public void settings() {
        size(800,400);

    }

    public void draw() {

        noStroke();
        point(40,30);
        fill(hotbitsHandler.nextInteger(255));
        rect(hotbitsHandler.nextInteger(400),hotbitsHandler.nextInteger(400),hotbitsHandler.nextInteger(40),hotbitsHandler.nextInteger(40));

        fill(random.nextInt(255));
        rect(400 + random.nextInt(400), random.nextInt(400),random.nextInt(40),random.nextInt(40));
        //point(hotbitsHandler.nextInteger(400),hotbitsHandler.nextInteger(400));

    }
}
