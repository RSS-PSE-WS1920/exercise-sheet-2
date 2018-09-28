package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.EditorHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.ReadOnlyHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification.AddGrainsToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification.AddWallToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification.ClearTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification.InitDefaultHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification.InitializeTerritoryCommandSpecification;
import de.unistuttgart.iste.rss.utils.LambdaVisitor;

public class EditorTerritory extends ReadOnlyTerritory {

    private final Function<CommandSpecification, Command> editCommandFactory;

    public EditorTerritory() {
        super();
        editCommandFactory = new LambdaVisitor<CommandSpecification, Command>().
                on(InitializeTerritoryCommandSpecification.class).then(this::createInitializeTerritoryCommand).
                on(AddGrainsToTileCommandSpecification.class).then(this::createAddGrainsToTileCommand).
                on(AddWallToTileCommandSpecification.class).then(this::createAddWallToTileCommand).
                on(InitDefaultHamsterCommandSpecification.class).then(this::createInitDefaultHamsterCommand).
                on(ClearTileCommandSpecification.class).then(this::createClearTileCommand);
    }

    @Override
    public EditorHamster getDefaultHamster() {
        return (EditorHamster) this.defaultHamster.get();
    }

    public Optional<Command> getCommandFromSpecification(final CommandSpecification spec) {
        return Optional.ofNullable(this.editCommandFactory.apply(spec));
    }

    @Override
    protected ReadOnlyHamster initDefaultHamster() {
        return new EditorHamster();
    }
    
    private Command createInitDefaultHamsterCommand(final InitDefaultHamsterCommandSpecification specification) {
        return new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                final Tile tile = getTileAt(specification.getLocation());
                builder.newSetPropertyCommand(getDefaultHamster().currentTile, Optional.of(tile));
                builder.newAddToPropertyCommand(tile.content, getDefaultHamster());
            }
        };
    }
    
    private Command createClearTileCommand(final ClearTileCommandSpecification specification) {
        return new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                final Tile tile = getTileAt(specification.getLocation());
                tile.content.stream().
                        filter(content -> !ReadOnlyHamster.class.isInstance(content)).
                        forEach(content -> builder.newRemoveFromPropertyCommand(tile.content, content));
            }
        };
    }

    private Command createAddWallToTileCommand(final AddWallToTileCommandSpecification specification) {
        return new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                final Tile tile = getTileAt(specification.getLocation());
                final TileContent newWall = new Wall();
                builder.newAddToPropertyCommand(tile.content, newWall);
                builder.newSetPropertyCommand(newWall.currentTile, Optional.of(tile));
            }
        };
    }

    private Command createAddGrainsToTileCommand(final AddGrainsToTileCommandSpecification specification) {
        return new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                final Tile tile = getTileAt(specification.getLocation());
                for (int i = 0; i < specification.getAmount(); i++) {
                    final TileContent newContent = new Grain();
                    builder.newAddToPropertyCommand(tile.content, newContent);
                    builder.newSetPropertyCommand(newContent.currentTile, Optional.of(tile));
                }
            }
        };
    }

    private Command createInitializeTerritoryCommand(final InitializeTerritoryCommandSpecification spec) {
        return new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                if (getSize().getRowCount() > 0 && getSize().getColumnCount() > 0) {
                    getAllLocations(getSize()).
                        map(location -> getTileAt(location)).
                        forEach(tile -> builder.newRemoveFromPropertyCommand(tiles, tile));
                }
                builder.newSetPropertyCommand(territorySize, spec.getSize());
                getAllLocations(spec.getSize()).
                    forEach(location -> builder.newAddToPropertyCommand(tiles, new Tile(EditorTerritory.this, location)));
                hamsters.filtered(h -> h != defaultHamster.get()).
                    forEach(h -> builder.newRemoveFromPropertyCommand(hamsters, h));
            }
        };
    }

    private Stream<Location> getAllLocations(final Size territoryDimension) {
        return Location.getAllLocationsFromTo(
                Location.ORIGIN,
                Location.from(territoryDimension.getRowCount() - 1, territoryDimension.getColumnCount() - 1));
    }
}
