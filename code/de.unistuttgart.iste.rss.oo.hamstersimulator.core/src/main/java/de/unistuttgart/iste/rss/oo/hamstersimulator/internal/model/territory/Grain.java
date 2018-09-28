package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory;

public class Grain extends TileContent {

    @Override
    public String toString() {
        return "Grain";
    }

    @Override
    protected boolean blocksEntrance() {
        return false;
    }
}