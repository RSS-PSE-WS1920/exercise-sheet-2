package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

public final class Size {

    private final int columnCount;
    private final int rowCount;

    public Size(final int columnCount, final int rowCount) {
        super();
        this.columnCount = columnCount;
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

}
