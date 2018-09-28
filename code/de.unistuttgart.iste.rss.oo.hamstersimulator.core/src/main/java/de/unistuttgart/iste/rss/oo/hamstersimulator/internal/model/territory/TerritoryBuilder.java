package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory;

import java.util.LinkedList;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification.AddGrainsToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification.AddWallToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification.InitDefaultHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification.InitializeTerritoryCommandSpecification;

public class TerritoryBuilder {

    private final EditorTerritory territory;
    private final LinkedList<Command> commands = new LinkedList<>();

    TerritoryBuilder(final EditorTerritory territory) {
        super();
        this.territory = territory;
    }

    public TerritoryBuilder initializeTerritory(final Size size) {
        this.commands.add(this.territory.getCommandFromSpecification(new InitializeTerritoryCommandSpecification(size)).get());
        return this;
    }

    public TerritoryBuilder wallAt(final Location location) {
        this.commands.add(this.territory.getCommandFromSpecification(new AddWallToTileCommandSpecification(location)).get());
        return this;
    }

    public TerritoryBuilder defaultHamsterAt(final Location location, final Direction direction, final int grainCount) {
        final CommandSpecification specification = new InitDefaultHamsterCommandSpecification(location, direction, grainCount);
        this.commands.add(this.territory.getCommandFromSpecification(specification).get());
        this.commands.add(this.territory.getDefaultHamster().getCommandFromSpecification(specification).get());
        return this;
    }

    public TerritoryBuilder grainAt(final Location location, final int grainCount) {
        this.commands.add(this.territory.getCommandFromSpecification(new AddGrainsToTileCommandSpecification(location, grainCount)).get());
        return this;
    }

    public TerritoryBuilder grainAt(final Location location) {
        return this.grainAt(location, 1);
    }

    public Command build() {
        return new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                builder.add(commands);
            }
        };
    }

}
