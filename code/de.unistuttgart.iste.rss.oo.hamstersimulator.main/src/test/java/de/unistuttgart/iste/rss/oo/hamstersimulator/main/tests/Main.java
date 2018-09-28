package de.unistuttgart.iste.rss.oo.hamstersimulator.main.tests;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

/**
 * Simple test run of the hamster game API examplifying the use of the objects.
 * @author Steffen Becker
 *
 */
public final class Main {

    /**
     * Main method which instantiates, initializes, and starts a hamster game.
     * @param args Default command line parameters, not used.
     */
    public static void main(final String[] args) {
        JavaFXUI.start();

        final HamsterGame game = new HamsterGame();
        game.initialize();
        game.setInputInterface(JavaFXUI.getJavaFXInputInterface());

        game.displayInNewGameWindow();
        game.runGame(Main::exampleRun);
    }

    /**
     * Hamster main method, executed by this test.
     * @param territory The territory used during this test.
     */
    public static void exampleRun(final Territory territory) {
        final Hamster paule = territory.getDefaultHamster();
        final Hamster willi = new Hamster(territory, Location.from(1, 3), Direction.WEST, 0);
        final Hamster marry = new Hamster(territory, Location.from(1, 2), Direction.EAST, 0);

        paule.write("Hallo!");
        final String text = paule.readString("Please give my any text!");
        paule.write(text);
        final int value = paule.readNumber("Please give my a positive number!");
        paule.write(Integer.toString(value));
        while (!paule.grainAvailable() && paule.frontIsClear()) {
            paule.move();
        }
        while (paule.grainAvailable()) {
            paule.pickGrain();
        }
        paule.turnLeft();
        paule.turnLeft();
        while (paule.frontIsClear()) {
            paule.move();
        }
        while (!paule.mouthEmpty()) {
            paule.putGrain();
        }
        willi.move();
        willi.write("Hallo auch von mir!");
        marry.move();
        marry.write("Ich auch!");
        paule.turnLeft();
        paule.move();
    }

    /**
     * Default constructor, only the VM should create instances of this.
     */
    private Main() { }
}
