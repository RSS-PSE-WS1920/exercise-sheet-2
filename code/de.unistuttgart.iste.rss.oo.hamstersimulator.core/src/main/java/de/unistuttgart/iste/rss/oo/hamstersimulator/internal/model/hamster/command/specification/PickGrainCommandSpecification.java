package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Grain;

public class PickGrainCommandSpecification extends AbstractHamsterCommandSpecification {

    private final Grain grain;
    
    public PickGrainCommandSpecification(final GameHamster hamster, final Grain grain) {
        super(hamster);
        this.grain = grain;
    }

    public Grain getGrain() {
        return grain;
    }
    
}
