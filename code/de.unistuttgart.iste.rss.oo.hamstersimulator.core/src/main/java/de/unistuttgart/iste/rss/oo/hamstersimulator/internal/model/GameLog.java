package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.util.Optional;
import java.util.function.Function;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LogEntry;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.InitHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.MoveCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.PickGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.PutGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.TurnLeftCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.WriteCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.properties.ModifyPropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.properties.ModifyPropertyCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.properties.ModifyPropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.utils.LambdaVisitor;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GameLog {
    
    final ReadOnlyListWrapper<LogEntry> gameLog = new ReadOnlyListWrapper<LogEntry>(this, "log", FXCollections.observableArrayList());
    private final Function<CommandSpecification, Command> editCommandFactory;

    public GameLog() {
        super();
        editCommandFactory = new LambdaVisitor<CommandSpecification, Command>().
                on(MoveCommandSpecification.class).then(s -> getLogCommand(s.getHamster(), "Move")).
                on(TurnLeftCommandSpecification.class).then(s -> getLogCommand(s.getHamster(), "Turn Left")).
                on(InitHamsterCommandSpecification.class).then(s -> getLogCommand(null, "Init Hamster")).
                on(PickGrainCommandSpecification.class).then(s -> getLogCommand(s.getHamster(), "Pick Grain")).
                on(PutGrainCommandSpecification.class).then(s -> getLogCommand(s.getHamster(), "Put Grain")).
                on(WriteCommandSpecification.class).then(s -> getLogCommand(s.getHamster(), s.getMessage()));
    }
    
    public ReadOnlyListProperty<LogEntry> logProperty() {
        return this.gameLog.getReadOnlyProperty();
    }

    public Optional<Command> getCommandFromSpecification(final CommandSpecification specification) {
        return Optional.ofNullable(editCommandFactory.apply(specification));
    }
    
    private Command getLogCommand(final GameHamster hamster, final String message) {
        return new ModifyPropertyCommand<ObservableList<LogEntry>>(gameLog, new ModifyPropertyCommandSpecification(new LogEntry(hamster, message), ActionKind.ADD));
    }
}
