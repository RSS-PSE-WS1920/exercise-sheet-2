package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.Collections;
import java.util.List;

public abstract class Command {
    
    protected abstract void execute();
    protected abstract void undo();

    public List<RuntimeException> getExceptionsFromPreconditions() {
        return Collections.emptyList();
    }
    
    public boolean canExecute() {
        return getExceptionsFromPreconditions().isEmpty();
    }
    
}
