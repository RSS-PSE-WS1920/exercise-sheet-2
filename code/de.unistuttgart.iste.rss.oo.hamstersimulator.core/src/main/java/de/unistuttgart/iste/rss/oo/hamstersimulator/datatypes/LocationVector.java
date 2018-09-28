package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

public final class LocationVector {
    private final int deltaRow;
    private final int delteColumn;
    public LocationVector(final int deltaRow, final int delteColumn) {
        super();
        this.deltaRow = deltaRow;
        this.delteColumn = delteColumn;
    }
    public int getDeltaRow() {
        return deltaRow;
    }
    public int getDelteColumn() {
        return delteColumn;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + deltaRow;
        result = prime * result + delteColumn;
        return result;
    }
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LocationVector other = (LocationVector) obj;
        if (deltaRow != other.deltaRow) {
            return false;
        }
        if (delteColumn != other.delteColumn) {
            return false;
        }
        return true;
    }
}
