package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Immutable class representing a location in the hamster territory.
 * @author Steffen Becker
 *
 */
public final class Location {

    /**
     * Constant representing the origin of the coordinate system. 
     */
    public static final Location ORIGIN = Location.from(0,0);

    private final int row;
    private final int column;

    public Location(final int row, final int column) {
        super();
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Negative row/column not allowed.");
        }
        this.row = row;
        this.column = column;
    }

    public /*@ pure @*/ int getRow() {
        return row;
    }

    public /*@ pure @*/ int getColumn() {
        return column;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + row;
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
        final Location other = (Location) obj;
        if (column != other.column) {
            return false;
        }
        if (row != other.row) {
            return false;
        }
        return true;
    }

    public Location translate(final LocationVector movementVector) {
        return new Location(this.row + movementVector.getDeltaRow(), this.column + movementVector.getDelteColumn());
    }

    public static Location from(final int row, final int column) {
        return new Location(row, column);
    }

    public static Stream<Location> getAllLocationsFromTo(final Location from, final Location to) {
        final Stream<Stream<Location>> stream = IntStream.range(from.getRow(), to.getRow()+1).mapToObj(row -> IntStream.range(from.getColumn(), to.getColumn()+1).mapToObj(column -> Location.from(row, column)));
        return stream.flatMap(s -> s);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Location [row=" + row + ", column=" + column + "]";
    }
}