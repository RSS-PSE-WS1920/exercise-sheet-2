package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;

public abstract class AbstractHamsterCommandSpecification implements CommandSpecification {
    private final GameHamster hamster;
    
    public AbstractHamsterCommandSpecification(final GameHamster hamster) {
        super();
        this.hamster = hamster;
    }
    
    public GameHamster getHamster() {
        return hamster;
    }

}
