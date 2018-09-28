package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkArgument;

import java.util.Collections;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.ReadOnlyHamster;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

public class ReadOnlyTerritory {

    final ReadOnlyObjectWrapper<Size> territorySize = new ReadOnlyObjectWrapper<Size>(this, "territorySize", new Size(0, 0));
    final ReadOnlyListWrapper<Tile> tiles = new ReadOnlyListWrapper<Tile>(this, "tiles", FXCollections.observableArrayList());
    final ReadOnlyObjectWrapper<ReadOnlyHamster> defaultHamster = new ReadOnlyObjectWrapper<ReadOnlyHamster>(this, "defaultHamster", initDefaultHamster());
    final ReadOnlyListWrapper<ReadOnlyHamster> hamsters = new ReadOnlyListWrapper<ReadOnlyHamster>(this, "hamsters", FXCollections.observableArrayList(defaultHamster.get()));

    public Size getSize() {
        return this.territorySize.get();
    }

    public ReadOnlyObjectProperty<Size> territorySizeProperty() {
        return this.territorySize.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<Tile> tilesProperty() {
        return this.tiles.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<ReadOnlyHamster> hamstersProperty() {
        return this.hamsters.getReadOnlyProperty();
    }

    public List<ReadOnlyHamster> getHamsters() {
        return Collections.unmodifiableList(this.hamsters.get());
    }

    public ReadOnlyHamster getDefaultHamster() {
        return this.defaultHamster.get();
    }

    public ReadOnlyObjectProperty<ReadOnlyHamster> defaultHamsterProperty() {
        return this.defaultHamster.getReadOnlyProperty();
    }

    public Tile getTileAt(final Location location) {
        checkArgument(isLocationInTerritory(location), "Location has to be in territory!");

        return tiles.get(getListIndexFromLocation(location));
    }

    public boolean isLocationInTerritory(final Location newHamsterPosition) {
        return newHamsterPosition.getColumn() < this.territorySize.get().getColumnCount() &&
                newHamsterPosition.getRow() < this.territorySize.get().getRowCount();
    }

    protected ReadOnlyHamster initDefaultHamster() {
        return new ReadOnlyHamster();
    }

    private int getListIndexFromLocation(final Location location) {
        return location.getRow() * this.territorySize.get().getColumnCount() + location.getColumn();
    }
}