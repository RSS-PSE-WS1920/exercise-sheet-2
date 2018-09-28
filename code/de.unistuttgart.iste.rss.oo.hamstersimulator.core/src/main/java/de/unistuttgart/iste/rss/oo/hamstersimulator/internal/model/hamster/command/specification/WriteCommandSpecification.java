package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;

public class WriteCommandSpecification extends AbstractHamsterCommandSpecification implements CommandSpecification {

    private final String message;

    public WriteCommandSpecification(final GameHamster hamster, final String message) {
        super(hamster);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
