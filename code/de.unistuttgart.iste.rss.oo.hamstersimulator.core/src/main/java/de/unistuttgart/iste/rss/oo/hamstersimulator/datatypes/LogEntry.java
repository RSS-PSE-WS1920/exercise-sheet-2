package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;

public class LogEntry {

    private final String message;
    private final GameHamster hamster;
    
    public LogEntry(final GameHamster hamster, final String message) {
        super();
        this.hamster = hamster;
        this.message = message;
    }

    public GameHamster getHamster() {
        return hamster;
    }

    public String getMessage() {
        return message;
    }
}
