package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public class InitDefaultHamsterCommandSpecification extends AbstractTerritoryTileCommandSpecification implements CommandSpecification {

    private final Direction direction;
    private final int grainCount;
    
    public InitDefaultHamsterCommandSpecification(final Location location, final Direction direction, final int grainCount) {
        super(location);
        this.direction = direction;
        this.grainCount = grainCount;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public int getGrainCount() {
        return grainCount;
    }
    
}
