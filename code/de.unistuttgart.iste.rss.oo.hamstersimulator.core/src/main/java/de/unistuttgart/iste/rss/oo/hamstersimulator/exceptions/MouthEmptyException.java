package de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions;

public class MouthEmptyException extends HamsterException {

    /**
     * 
     */
    private static final long serialVersionUID = -1788656994945039819L;

    public MouthEmptyException() {
        super("Tried to drop a grain, but hamster mouth was empty");
    }

}
