package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public final class AddGrainsToTileCommandSpecification extends AbstractTerritoryTileCommandSpecification implements CommandSpecification {

    private final int amount;

    public AddGrainsToTileCommandSpecification(final Location location, final int amount) {
        super(location);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

}
