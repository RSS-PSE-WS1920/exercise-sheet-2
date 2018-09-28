package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.ReadOnlyHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.GameTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryLoader;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Tile;

/**
 * The territory class represents territories for hamsters. Territories
 * are rectangular areas with width and height composed out of territory
 * tiles. Tiles again can contain hamster objects, grain objects, or walls.
 * This class provides methods to load or build a territory and to query its
 * current state.
 * @author Steffen Becker
 *
 */
public class Territory {
    //@ public instance invariant getTotalGrainCount() >= 0;
    //@ public instance invariant getTotalHamsterCount() > 0;

    /**
     * Map to translate from internal to external hamster.
     */
    private final Map<ReadOnlyHamster, Hamster> hamsterTranslation = new HashMap<>();

    /**
     * The parent hamste game to which this territory belongs to.
     */
    private final HamsterGame hamsterGame;

    /**
     * Internal model representation of the territory.
     */
    private final GameTerritory internalTerritory;

    /**
     * Reference to the default hamster of this territory.
     */
    private final Hamster defaultHamster;

    /**
     * Initialize a new territory and link it to its game object.
     * @param game The game in which this territory object is used.
     */
    public Territory(final HamsterGame game) {
        super();
        this.hamsterGame = game;
        this.internalTerritory = new GameTerritory();
        this.defaultHamster = Hamster.fromInternalDefaultHamster(this);
    }

    /**
     * Gets the default hamster of this territory. A default hamster always
     * exists on a territory, so this method never returns null.
     * @return The hamster object representing the default hamster of the
     *         territory.
     */
    public Hamster getDefaultHamster() {
        return this.defaultHamster;
    }

    /**
     * Gets the hamster game associated to this territory object.
     * @return The hamster game associated to this territory. The default hamster
     *         object is never null.
     */
    public HamsterGame getGame() {
        return this.hamsterGame;
    }

    /**
     * For a given location, tests whether the location is inside the bound of the territory.
     * @param location The location to test for.
     * @return true if the location is inside the territory.
     */
    public /*@ pure @*/ boolean isLocationInTerritory(final Location location) {
        return this.internalTerritory.isLocationInTerritory(location);
    }

    /**
     * Loads the territory from a file. The territory is initalized according to the loaded territory
     * file and the default hamster is initialized.
     * @param territoryFile The territory file path from where to load the territory. The path has to be relative
     *                      to the class path of this class and is loaded via the classes' class loader.
     */
    public void loadFromFile(final String territoryFile) {
        checkState(
                this.getGame().getCurrentGameMode() != Mode.RUNNING
                && this.getGame().getCurrentGameMode() != Mode.PAUSED);
        this.hamsterTranslation.clear();
        TerritoryLoader.initializeFor(this.internalTerritory).loadFromFile(territoryFile);
    }

    /**
     * Get all hamsters currently active in the territory.
     * @return A ordered list of all active hamsters.
     */
    public List<Hamster> getHamsters() {
        return this.internalTerritory.getHamsters().stream().
                map(internalHamster -> this.hamsterTranslation.get(internalHamster)).
                collect(Collectors.toList());
    }

    /**
     * Return the size of the current territory. From the size
     * you can retrieve the number of rows and colums.
     * @return A size object representing the size of the territory.
     *         Is never null.
     */
    public Size getTerritorySize() {
        return this.internalTerritory.getSize();
    }

    /**
     * Returns whether the tile at the given location is a wall.
     * @param location The location of the tile to check for a wall.
     * @return True if the tile at the given location is blocked by a wall.
     */
    public /*@ pure @*/ boolean isBlockedByWall(final Location location) {
        return this.internalTerritory.getTileAt(location).isBlocked();
    }

    /**
     * Get the number of grains at the given location.
     * @param location The location of the tile to check for grain.
     * @return The number of grains currently available at the given location.
     */
    public int getNumberOfGrainsAt(final Location location) {
        final Tile tile = this.internalTerritory.getTileAt(location);
        return tile.getGrainCount();
    }

    /**
     * Count all grains currently lying freely on any tile in the territory.
     * @return The total grain count of the territory excluding grains in the
     *         mouths of hamsters.
     */
    public /*@ pure @*/ int getTotalGrainCount() {
        int result = 0;
        for (int row = 0; row < getTerritorySize().getRowCount(); row++) {
            for (int column = 0; column < getTerritorySize().getColumnCount(); column++) {
                result += getNumberOfGrainsAt(Location.from(row, column));
            }
        }
        return result;
    }

    /**
     * Get the total number of hamsters on the current territory.
     * @return The total number of hamsters on the current territory.
     */
    public /*@ pure @*/ int getTotalHamsterCount() {
        return getHamsters().size();
    }

    /**
     * Get a list of hamsters at the given location. Might be empty.
     * @param location The location of the tile for which all its hamsters are
     *                 returned.
     * @return A list of hamsters on the respective tile. Might be an empty list, but…
     *         never null.
     */
    public List<Hamster> getHamstersAt(final Location location) {
        final Tile tile = this.internalTerritory.getTileAt(location);
        return tile.getHamsters().stream().
                map(internalHamster -> this.hamsterTranslation.get(internalHamster)).
                collect(Collectors.toList());
    }

    /**
     * Get the internal representation of the territory.
     * @return The internal territory object. This object is never null.
     */
    GameTerritory getInternalTerritory() {
        return this.internalTerritory;
    }

    /**
     * Internal method to keep track of all active hamsters.
     * @param externalHamster An external hamster object.
     * @param internalHamster An internal hamster object.
     */
    void registerHamster(final Hamster externalHamster, final ReadOnlyHamster internalHamster) {
        this.hamsterTranslation.put(internalHamster, externalHamster);
    }

}
