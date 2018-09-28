package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

abstract class AbstractTerritoryTileCommandSpecification {

    protected final Location location;

    public AbstractTerritoryTileCommandSpecification(final Location location) {
        super();
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

}