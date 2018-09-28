package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.unistuttgart.iste.rss.oo.hamstersimulator.properties.ModifyPropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.properties.ModifyPropertyCommandSpecification.ActionKind;
import javafx.beans.property.Property;

public class CompositeCommand extends Command {

    private List<Command> commandsToExecute = new LinkedList<>();
    private final CompositeCommandBuilder compositeCommandBuilder = new CompositeCommandBuilder();
    private final PreconditionBuilder preconditionBuilder = new PreconditionBuilder();
    private boolean isBuilt = false;
    private Consumer<CompositeCommandBuilder> commandConstructor = null;
    private Consumer<PreconditionBuilder> preconditionConstructor = null;

    public CompositeCommand setCommandConstructor(final Consumer<CompositeCommandBuilder> constructor) {
        this.commandConstructor = constructor;
        return this;
    }

    public CompositeCommand setPreconditionConstructor(final Consumer<PreconditionBuilder> preconditionConstructor) {
        this.preconditionConstructor = preconditionConstructor;
        buildPreconditions(this.preconditionBuilder);
        return this;
    }
    
    @Override
    public void execute() {
        if (!isBuilt) {
            buildBeforeFirstExecution(compositeCommandBuilder);
            isBuilt = true;
        }
        if (!getExceptionsFromPreconditions().isEmpty()) {
            throw getExceptionsFromPreconditions().get(0);
        }
        commandsToExecute = Collections.unmodifiableList(new ArrayList<>(compositeCommandBuilder.commandsToExecute));
        commandsToExecute.forEach(command -> command.execute());
    }

    @Override
    public List<RuntimeException> getExceptionsFromPreconditions() {
         final Function<Supplier<Optional<RuntimeException>>, Optional<RuntimeException>> exceptionSupplier =
                 supplier -> {
                     try {
                         return supplier.get();
                     } catch (final RuntimeException e) {
                         return Optional.of(e);
                     }
                 };
         final List<RuntimeException> result = preconditionBuilder.preconditions.stream().
                 map(exceptionSupplier).
                 filter(optionalException -> optionalException.isPresent()).
                 map(optionalException -> optionalException.get()).
                 collect(Collectors.toList());
         for (final Command command : commandsToExecute) {
             result.addAll(command.getExceptionsFromPreconditions());
         }
         return result;
    }
    
    @Override
    public void undo() {
        final List<Command> undoCommands = new ArrayList<>(commandsToExecute);
        Collections.reverse(undoCommands);
        undoCommands.stream().forEach(command -> command.undo());
    }

    public class CompositeCommandBuilder {
        private final List<Command> commandsToExecute = new LinkedList<>();

        private CompositeCommandBuilder() {}
        
        public CompositeCommandBuilder add(final List<Command> commands) {
            commandsToExecute.addAll(commands);
            return this;
        }

        public CompositeCommandBuilder add(final Command ... commands ) {
            for (final Command command : commands) {
                commandsToExecute.add(command);
            }
            return this;
        }
        
        public <G> void newSetPropertyCommand (final Property<G> property, final Object value) {
            this.add(ModifyPropertyCommand.createPropertyUpdateCommand(property, value, ActionKind.SET));
        }

        public <G> void newAddToPropertyCommand (final Property<G> property, final Object value) {
            this.add(ModifyPropertyCommand.createPropertyUpdateCommand(property, value, ActionKind.ADD));
        }

        public <G> void newRemoveFromPropertyCommand (final Property<G> property, final Object value) {
            this.add(ModifyPropertyCommand.createPropertyUpdateCommand(property, value, ActionKind.REMOVE));
        }
    }

    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        if (commandConstructor != null) {
            commandConstructor.accept(builder);
        }
    }
    
    public class PreconditionBuilder {
        private final List<Supplier<Optional<RuntimeException>>> preconditions = new LinkedList<>();

        public PreconditionBuilder addNewPrecondition(final Supplier<Optional<RuntimeException>> condition) {
            this.preconditions.add(condition);
            return this;
        }

        public PreconditionBuilder addNewPrecondition(final Supplier<RuntimeException> exceptionSupplier, final Supplier<Boolean> condition) {
            final Supplier<Optional<RuntimeException>> newCondition = new Supplier<Optional<RuntimeException>>() {

                @Override
                public Optional<RuntimeException> get() {
                    if (condition.get()) {
                        return Optional.of(exceptionSupplier.get());
                    } else {
                        return Optional.empty();
                    }
                }
            };
            this.preconditions.add(newCondition);
            return this;
        }

    }
    
    protected void buildPreconditions(final PreconditionBuilder builder) {
        if (preconditionConstructor != null) {
            preconditionConstructor.accept(builder);
        }
    }
}
