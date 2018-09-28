package de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions;

public class FrontBlockedException extends HamsterException {
    /**
     * 
     */
    private static final long serialVersionUID = -978786338765499216L;

    public FrontBlockedException() {
        super("Hamster front is blocked or outside territory");
    }
}
