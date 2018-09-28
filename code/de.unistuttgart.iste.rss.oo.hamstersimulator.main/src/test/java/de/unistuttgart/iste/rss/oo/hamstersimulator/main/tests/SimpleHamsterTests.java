package de.unistuttgart.iste.rss.oo.hamstersimulator.main.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions.FrontBlockedException;
import de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions.MouthEmptyException;
import de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions.NoGrainOnTileException;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;

/**
 * Simple tests for the hamster API.
 * @author Steffen Becker
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class SimpleHamsterTests {

    /**
     * Field containing the hamster game used in tests.
     */
    private HamsterGame game;

    /**
     * During a test, this contains a reference to the default hamster.
     */
    private Hamster paule;

    /**
     * Initialize a game and its UI.
     */
    @BeforeAll
    public void createHamsterGame() {
        game = new HamsterGame();
    }

    /**
     * Before each test, load the default territory.
     */
    @BeforeEach
    public void initializeTest() {
        game.initialize();
        paule = game.getTerritory().getDefaultHamster();
    }

    /**
     * Test which tests a single default hamster move.
     */
    @Test
    public void testMove() {
        final Location beforeLocation = paule.getLocation();
        paule.move();
        assertEquals(paule.getLocation(), Location.from(1, 2));
        assertEquals(paule.getLocation(), beforeLocation.translate(new LocationVector(0, 1)));
    }

    /**
     * Test for the hamster's turn command.
     */
    @Test
    public void testTurn() {
        assertEquals(paule.getDirection(), Direction.EAST);
        paule.turnLeft();
        assertEquals(paule.getDirection(), Direction.NORTH);
        paule.turnLeft();
        assertEquals(paule.getDirection(), Direction.WEST);
        paule.turnLeft();
        assertEquals(paule.getDirection(), Direction.SOUTH);
        paule.turnLeft();
        assertEquals(paule.getDirection(), Direction.EAST);
    }

    /**
     * Test which tests exception when running against a wall.
     */
    @Test
    public void testFailedMove() {
        assertThrows(FrontBlockedException.class, () -> {
            paule.turnLeft();
            paule.move();
        });
    }

    /**
     * Test picking up a non-existing grain.
     */
    @Test
    public void testFailedPickup() {
        assertThrows(NoGrainOnTileException.class, () -> {
            paule.pickGrain();
        });
    }

    /**
     * Test putting a non-existing grain.
     */
    @Test
    public void testFailedPut() {
        assertThrows(MouthEmptyException.class, () -> {
            paule.putGrain();
        });
    }

    /**
     * Test for spawning a new hamster and registering it at the
     * territory.
     */
    @Test
    public void testSpawnHamster() {
        final Location spawnLocation = Location.from(1, 2);
        assertEquals(game.getTerritory().getHamsters().size(), 1);
        assertEquals(paule, game.getTerritory().getHamsters().get(0));
        final Hamster marry = new Hamster(game.getTerritory(), spawnLocation, Direction.EAST, 0);
        assertEquals(game.getTerritory().getHamsters().size(), 2);
        marry.move();
        assertEquals(spawnLocation.translate(new LocationVector(0, 1)), marry.getLocation());
        assertEquals(marry, game.getTerritory().getHamsters().get(1));
        game.initialize();
        assertEquals(game.getTerritory().getHamsters().size(), 1);
        assertEquals(paule, game.getTerritory().getHamsters().get(0));
    }
}
