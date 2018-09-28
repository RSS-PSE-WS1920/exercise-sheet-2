package de.unistuttgart.iste.rss.oo.hamstersimulator.properties;

public final class ModifyPropertyCommandSpecification  {

    public enum ActionKind {
        SET, ADD, REMOVE
    }

    private final Object newValue;
    private final ActionKind actionKind;

    public ModifyPropertyCommandSpecification(final Object newValue, final ActionKind actionKind) {
        super();
        this.newValue = newValue;
        this.actionKind = actionKind;
    }

    public ActionKind getActionKind() {
        return actionKind;
    }

    public Object getNewValue() {
        return newValue;
    }

}
