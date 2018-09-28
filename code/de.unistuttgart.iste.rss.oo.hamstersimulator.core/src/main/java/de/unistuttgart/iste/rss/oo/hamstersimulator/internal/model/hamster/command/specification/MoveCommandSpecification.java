package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;

public class MoveCommandSpecification extends AbstractHamsterCommandSpecification implements CommandSpecification {

    public MoveCommandSpecification(final GameHamster hamster) {
        super(hamster);
    }

}
