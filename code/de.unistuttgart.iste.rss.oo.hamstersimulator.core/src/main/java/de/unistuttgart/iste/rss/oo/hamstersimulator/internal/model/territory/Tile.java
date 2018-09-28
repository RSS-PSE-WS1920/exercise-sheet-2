package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.ReadOnlyHamster;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

public class Tile {

    final ReadOnlyListWrapper<TileContent> content = new ReadOnlyListWrapper<TileContent>(this, "content", FXCollections.observableArrayList());
    final ReadOnlyIntegerWrapper grainCount = new ReadOnlyIntegerWrapper(this, "grainCount", 0);
    final ReadOnlyBooleanWrapper isBlocked = new ReadOnlyBooleanWrapper(this, "isBlocked", false);
    private final ReadOnlyListWrapper<TileContent> grainSublist;
    private final ReadOnlyListWrapper<TileContent> blockerSublist;
    private final ReadOnlyListWrapper<TileContent> hamsterSublist;

    private final ReadOnlyTerritory territory;
    private final Location tileLocation;

    Tile(final ReadOnlyTerritory territory, final Location tileLocation) {
        super();

        checkNotNull(territory);
        checkNotNull(tileLocation);

        this.territory = territory;
        this.tileLocation = tileLocation;

        this.grainSublist = new ReadOnlyListWrapper<TileContent>(new FilteredList<TileContent>(content, c -> c instanceof Grain));
        this.blockerSublist = new ReadOnlyListWrapper<TileContent>(new FilteredList<TileContent>(content, c -> c.blocksEntrance()));
        this.hamsterSublist = new ReadOnlyListWrapper<TileContent>(new FilteredList<TileContent>(content, c -> c instanceof ReadOnlyHamster));

        this.grainCount.bind(this.grainSublist.sizeProperty());
        this.isBlocked.bind(Bindings.createBooleanBinding(() -> this.blockerSublist.size() > 0, this.blockerSublist.sizeProperty()));
    }

    public ReadOnlyTerritory getTerritory() {
        return territory;
    }

    public /*@ pure @*/ Location getLocation() {
        return tileLocation;
    }

    public int getGrainCount() {
        return this.grainCount.get();
    }

    public boolean isBlocked() {
        return isBlocked.get();
    }

    public List<TileContent> getContent() {
        return Collections.unmodifiableList(this.content.get());
    }

    public List<? extends TileContent> getHamsters() {
        return Collections.unmodifiableList(this.hamsterSublist.get());
    }

    public ReadOnlyIntegerProperty grainCountProperty() {
        return this.grainCount.getReadOnlyProperty();
    }

    public ReadOnlyBooleanProperty isBlockedProperty() {
        return this.isBlocked.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<TileContent> contentProperty() {
        return this.content.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<TileContent> hamstersProperty() {
        return this.hamsterSublist.getReadOnlyProperty();
    }

    public void dispose() {
        final Collection<TileContent> content = new LinkedList<>(this.content);
        for (final TileContent item : content) {
            this.content.remove(item);
        }
    }

    @Override
    public String toString() {
        return "Tile [tileLocation=" + tileLocation + ", content=" + content + "]";
    }

}