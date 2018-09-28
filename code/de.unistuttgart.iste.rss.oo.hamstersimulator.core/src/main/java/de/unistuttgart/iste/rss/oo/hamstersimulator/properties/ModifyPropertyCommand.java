package de.unistuttgart.iste.rss.oo.hamstersimulator.properties;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.properties.ModifyPropertyCommandSpecification.ActionKind;
import javafx.beans.property.Property;
import javafx.beans.value.WritableListValue;
import javafx.beans.value.WritableSetValue;

public class ModifyPropertyCommand<T> extends Command {

    private Object oldValue;
    private final Property<T> property;
    private final ModifyPropertyCommandSpecification specification;

    public ModifyPropertyCommand(final Property<T> property, final ModifyPropertyCommandSpecification spec) {
        super();
        this.property = property;
        this.specification = spec;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void execute() {
        switch (this.specification.getActionKind()) {
        case SET:
            this.oldValue = property.getValue();
            property.setValue((T)this.specification.getNewValue());
            break;
        case ADD:
            if (property instanceof WritableSetValue) {
                ((WritableSetValue) property).add(this.specification.getNewValue());
            } else if (property instanceof WritableListValue) {
                ((WritableListValue) property).add(this.specification.getNewValue());
            } else {
                throw new UnsupportedOperationException();
            }
            break;
        case REMOVE:
            if (property instanceof WritableSetValue) {
                ((WritableSetValue) property).remove(this.specification.getNewValue());
            } else if (property instanceof WritableListValue) {
                ((WritableListValue) property).remove(this.specification.getNewValue());
            } else {
                throw new UnsupportedOperationException();
            }
            break;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void undo() {
        switch (this.specification.getActionKind()) {
        case SET:
            property.setValue((T)this.oldValue);
            break;
        case ADD:
            if (property instanceof WritableSetValue) {
                ((WritableSetValue) property).remove(this.specification.getNewValue());
            } else if (property instanceof WritableListValue) {
                ((WritableListValue) property).remove(this.specification.getNewValue());
            } else {
                throw new UnsupportedOperationException();
            }
            break;
        case REMOVE:
            if (property instanceof WritableSetValue) {
                ((WritableSetValue) property).add(this.specification.getNewValue());
            } else if (property instanceof WritableListValue) {
                ((WritableListValue) property).add(this.specification.getNewValue());
            } else {
                throw new UnsupportedOperationException();
            }
            break;
        }
    }

    public static <G> ModifyPropertyCommand<G> createPropertyUpdateCommand(
            final Property<G> property,
            final Object value,
            final ActionKind action) {
        return new ModifyPropertyCommand<G>(property, new ModifyPropertyCommandSpecification(value, action));
    }

}
