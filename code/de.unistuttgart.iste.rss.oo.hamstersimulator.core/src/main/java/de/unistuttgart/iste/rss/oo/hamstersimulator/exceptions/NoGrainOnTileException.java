package de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions;

public class NoGrainOnTileException extends HamsterException {

    /**
     * 
     */
    private static final long serialVersionUID = 3919325269698230381L;

    public NoGrainOnTileException() {
        super("The grain to be picked up does not exist on the tile");
    }

}
