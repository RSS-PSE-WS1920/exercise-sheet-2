package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkNotNull;
import static de.unistuttgart.iste.rss.utils.Preconditions.checkState;

import java.util.Optional;
import java.util.function.Consumer;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.EditCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.GameLog;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.InputInterface;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.AbstractHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryLoader;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

/**
 * A class representing an instance of a hamster game. A hamster game consists of a territory, on
 * which the game takes place, a game log for messages, and a command stack for keeping track of the
 * game's history.
 * @author Steffen Becker
 *
 */
public class HamsterGame {

    /**
     * Constant containing the filename of the default territory file.
     */
    private static final String DEFAULT_HAMSTER_TERRITORY = "/territories/example01.ter";

    /**
     * Game log object used to print log messages from the game and write commands from hamsters.
     */
    private final GameLog log = new GameLog();

    /**
     * Game command stack object used to execute game commands, i.e., commands comming from
     * the hamster objects on the territory during the simulation run.
     */
    private final GameCommandStack commandStack = new GameCommandStack();

    /**
     * The territory object on which the instance of this game takes place.
     */
    private final Territory territory = new Territory(this);

    /**
     * The input interface used when hamsters ask for input.
     */
    private InputInterface inputInterface;

    /**
     * Getter for the territory object of this game. Cannot be null.
     * @return The territory object of this game.
     */
    public Territory getTerritory() {
        return territory;
    }

    /**
     * Getter for the game log. Cannot be null.
     * @return The game log of this hamster game's instance.
     */
    public GameLog getGameLog() {
        return log;
    }

    /**
     * Initialize a new hamster game by loading the default territory.
     */
    public void initialize() {
        initialize(DEFAULT_HAMSTER_TERRITORY);
    }

    /**
     * Initialize a new hamster game by loading the territory from the passed
     * territory file path.
     * @param territoryFile The territory file path. Has to be a location relative to
     *                      the classes' class path.
     */
    public void initialize(final String territoryFile) {
        new EditCommandStack().execute(
                TerritoryLoader.initializeFor(territory.getInternalTerritory()).loadFromFile(territoryFile));
    }

    /**
     * Reset the hamster game to its initial state. Removes all hamsters besides  the
     * default hamster and places all gain objects to their initial position.
     */
    public void reset() {
        this.commandStack.undoAll();
        this.commandStack.reset();
    }

    /**
     * Gets the input interface of this game used to read values from users
     * or mock objects.
     * @return The input interface for this game.
     */
    public InputInterface getInputInterface() {
        checkState(this.inputInterface != null, "Input interface needs to be defined first!");
        return this.inputInterface;
    }

    /**
     * Sets this game's input interface for reading values from users or
     * mock objects.
     * @param newInputInterface The new input interface.
     */
    public void setInputInterface(final InputInterface newInputInterface) {
        checkNotNull(newInputInterface);
        this.inputInterface = newInputInterface;
    }

    /**
     * Opens a new Game UI for this game object. The game UI shows
     * the game and its current state while the game is executing.
     */
    public void displayInNewGameWindow() {
        JavaFXUI.openSceneFor(this.territory.getInternalTerritory(), this.commandStack, this.getGameLog());
    }

    /**
     * Run a given hamster program until it terminates.
     * @param hamsterProgram The hamster programm to run.
     */
    public void runGame(final Consumer<Territory> hamsterProgram) {
        startGame(true);
        try {
            hamsterProgram.accept(this.territory);
        } catch (final RuntimeException e) {
            this.inputInterface.showAlert(e);
        }
        stopGame();
    }

    /**
     * Start the execution of a hamster game. Before executing start, no commands can be
     * executed by the hamster objects in the game.
     * @param startPaused if true the game will be started in pause mode
     */
    public void startGame(final boolean startPaused) {
        this.commandStack.startGame(startPaused);
    }

    /**
     * Stop the execution of the game. The game is finished and needs to be reseted
     * or closed.
     */
    public void stopGame() {
        this.commandStack.stopGame();
    }

    /**
     * Get the current state of this game.
     * @return The current state of this game.
     */
    public Mode getCurrentGameMode() {
        return this.commandStack.stateProperty().get();
    }

    /**
     * This is a central method of the hamster simulation engine. It implements the mediator pattern.
     * It accepts command specifications of game commands and distributes it to all game entities for
     * their partial execution. For example, each command specification is sent to the game log so that
     * it can create an appropriate log entry.
     * @param specification The command specification of the command to be executed in this game.
     */
    void processCommandSpecification(final CommandSpecification specification) {
        final Optional<Command> territoryCommandPart =
                territory.getInternalTerritory().getCommandFromSpecification(specification);
        final Optional<Command> logCommandPart = this.log.getCommandFromSpecification(specification);
        final Optional<Command> hamsterPart;
        if (specification instanceof AbstractHamsterCommandSpecification) {
            final AbstractHamsterCommandSpecification hamsterCommandSpec =
                    (AbstractHamsterCommandSpecification) specification;
            hamsterPart = hamsterCommandSpec.getHamster().getCommandFromSpecification(specification);
        } else {
            hamsterPart = Optional.empty();
        }
        final Command composite = new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                if (territoryCommandPart.isPresent()) {
                    builder.add(territoryCommandPart.get());
                }
                if (logCommandPart.isPresent()) {
                    builder.add(logCommandPart.get());
                }
                if (hamsterPart.isPresent()) {
                    builder.add(hamsterPart.get());
                }
            }
        };
        this.commandStack.execute(composite);
    }
}
