package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.ReadOnlyHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.ReadOnlyTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Tile;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

public class HamsterTerritoryGrid extends StackPane {

    private static final double MINIMUM_TILE_SIZE = 20.0;

    final Map<ReadOnlyHamster,Integer> hamsterToColorPos = new HashMap<>();

    private final SimpleObjectProperty<Size> gridSize = new SimpleObjectProperty<Size>(this, "gridSize", new Size(0,0));
    private final ReadOnlyListWrapper<TileNode> cells = new ReadOnlyListWrapper<TileNode>(this, "cells", FXCollections.observableArrayList());
    private final GridPane hamsterGrid;
    private ReadOnlyTerritory territory;
    private NumberBinding squaredSize;

    private final ListChangeListener<Tile> tilesChangedListener = new ListChangeListener<Tile>() {
        @Override
        public void onChanged(final Change<? extends Tile> change) {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(tile -> addTileNode(tile));
                }
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(tile -> removeTileNode(tile));
                }
            }
        }
    };


    public HamsterTerritoryGrid() {
        super();
        this.getStyleClass().add("game-grid");
        this.setAlignment(Pos.CENTER);
        this.hamsterGrid = new GridPane();
        this.hamsterGrid.getStyleClass().add("game-grid");
        this.getChildren().add(this.hamsterGrid);
        configureSquareSizedTiles(this.gridSize.get());
        this.gridSize.addListener((obj, oldValue, newValue) -> {
            configureSquareSizedTiles(newValue);
            hamsterToColorPos.clear();
            this.territory.tilesProperty().forEach(tile -> addTileNode(tile));
        });
    }

    public void bindToTerritory(final ReadOnlyTerritory territory) {
        this.territory = territory;
        this.gridSize.bind(this.territory.territorySizeProperty());
        this.territory.tilesProperty().addListener(tilesChangedListener);
    }

    public void unbind() {
        this.gridSize.unbind();
        this.territory.tilesProperty().removeListener(tilesChangedListener);
    }

    private void configureSquareSizedTiles(final Size size) {
        final int columns = size.getColumnCount();
        final int rows = size.getRowCount();
        final NumberBinding pixPerCellWidth = this.widthProperty().divide(columns == 0 ? 1 : columns);
        final NumberBinding pixPerCellHeight = this.heightProperty().divide(rows == 0 ? 1 : rows);
        this.squaredSize = Bindings.min(pixPerCellHeight, pixPerCellWidth);

        this.hamsterGrid.getColumnConstraints().clear();
        for (int i = 0; i < columns; i++) {
            final ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / columns);
            this.hamsterGrid.getColumnConstraints().add(column);
        }

        this.hamsterGrid.getRowConstraints().clear();
        for (int i = 0; i < rows; i++) {
            final RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / rows);
            this.hamsterGrid.getRowConstraints().add(row);
        }

        this.hamsterGrid.maxWidthProperty().bind(squaredSize.multiply(columns));
        this.hamsterGrid.maxHeightProperty().bind(squaredSize.multiply(rows));

        this.hamsterGrid.setAlignment(Pos.CENTER);
    }

    private void addTileNode(final Tile tile) {
        final Location location = tile.getLocation();
        setTileNodeAt(location, new TileNode(this, tile));
    }

    private void removeTileNode(final Tile tile) {
        getTileNodeAt(tile.getLocation()).dispose();
        JavaFXUtil.blockingExecuteOnFXThread(() -> this.hamsterGrid.getChildren().remove(getTileNodeAt(tile.getLocation())));
        setTileNodeAt(tile.getLocation(), null);
    }

    private TileNode getTileNodeAt(final Location location) {
        return this.cells.get(location.getRow() * this.gridSize.get().getColumnCount() + location.getColumn());
    }

    private void setTileNodeAt(final Location location, final TileNode node) {
        final int index = location.getRow() * this.gridSize.get().getColumnCount() + location.getColumn();
        if (index < this.cells.getSize()) {
            this.cells.set(index, node);
        } else {
            this.cells.add(index, node);
        }
        if (node != null) {
            node.prefWidthProperty().bind(this.squaredSize);
            node.prefHeightProperty().bind(this.squaredSize);
            node.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
            node.setMinSize(MINIMUM_TILE_SIZE, MINIMUM_TILE_SIZE);
            Platform.runLater(() -> this.hamsterGrid.add(node, location.getColumn(), location.getRow()));
        }
    }

}
