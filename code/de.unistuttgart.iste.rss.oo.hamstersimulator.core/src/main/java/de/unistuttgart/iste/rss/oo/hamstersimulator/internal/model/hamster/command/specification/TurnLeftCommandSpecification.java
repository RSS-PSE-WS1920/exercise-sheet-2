package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;

public class TurnLeftCommandSpecification extends AbstractHamsterCommandSpecification implements CommandSpecification {

    public TurnLeftCommandSpecification(final GameHamster hamster) {
        super(hamster);
    }

}
