package de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions;

public class HamsterNotInitializedException extends HamsterException {

    /**
     * 
     */
    private static final long serialVersionUID = 8626237429095340318L;

    public HamsterNotInitializedException() {
        super("Trying to command a non-initialized hamster");
    }

}
