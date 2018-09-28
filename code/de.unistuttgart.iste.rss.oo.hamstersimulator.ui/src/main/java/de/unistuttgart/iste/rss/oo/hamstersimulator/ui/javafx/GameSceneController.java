package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LogEntry;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.GameLog;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.ReadOnlyTerritory;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class GameSceneController {

    class CellFormat extends ListCell<LogEntry> {
        @Override
        protected void updateItem(final LogEntry item, final boolean empty) {
            Platform.runLater(() -> {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMessage());
                    if (hamsterGrid.hamsterToColorPos.containsKey(item.getHamster())) {
                        setTextFill(TileNode.hamsterColors[hamsterGrid.hamsterToColorPos.get(item.getHamster())]);
                    } else {
                        setTextFill(Color.BLACK);
                    }
                }
            });
        }
    }

    @FXML private BorderPane root;
    @FXML private ToolBar toolbar;
    @FXML private Button play;
    @FXML private Button pause;
    @FXML private Button undo;
    @FXML private Button redo;
    @FXML private Slider speed;
    @FXML private HamsterTerritoryGrid hamsterGrid;
    @FXML private SplitPane splitPane;
    @FXML private ListView<LogEntry> log;

    private GameCommandStack commandStack;

    @FXML
    private void initialize() {
    }

    @FXML
    void pauseGame(final ActionEvent event) {
        commandStack.pause();
    }

    @FXML
    void undo(final ActionEvent event) {
        commandStack.undo();
    }

    @FXML
    void redo(final ActionEvent event) {
        commandStack.redo();
    }

    @FXML
    void startGame(final ActionEvent event) {
        commandStack.resume();
    }

    public void connectToGame(final ReadOnlyTerritory territory, final GameCommandStack commandStack, final GameLog gameLog) {
        this.commandStack = commandStack;
        this.hamsterGrid.bindToTerritory(territory);
        final BooleanBinding runningBinding = Bindings.createBooleanBinding(() -> commandStack.stateProperty().get() == Mode.RUNNING, commandStack.stateProperty());
        this.play.disableProperty().bind(Bindings.createBooleanBinding(() -> commandStack.stateProperty().get() == Mode.PAUSED, commandStack.stateProperty()).not());
        this.pause.disableProperty().bind(runningBinding.not());
        this.undo.disableProperty().bind(this.commandStack.canUndoProperty().not().or(runningBinding));
        this.redo.disableProperty().bind(this.commandStack.canRedoProperty().not().or(runningBinding));
        this.speed.valueProperty().bindBidirectional(this.commandStack.speedProperty());
        this.log.setCellFactory(new Callback<ListView<LogEntry>, ListCell<LogEntry>>() {
            @Override
            public ListCell<LogEntry> call(final ListView<LogEntry> list) {
                return new CellFormat();
            }
        });
        this.log.itemsProperty().bind(gameLog.logProperty());
        this.log.getItems().addListener(new ListChangeListener<LogEntry>(){

            @Override
            public void onChanged(final ListChangeListener.Change<? extends LogEntry> c) {
                Platform.runLater(() -> log.scrollTo(c.getList().size()-1));
            }
        });
    }

}

