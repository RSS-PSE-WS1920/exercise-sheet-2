package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

public enum Direction {
    NORTH (new LocationVector(-1,0)),
    SOUTH (new LocationVector(1,0)),
    WEST (new LocationVector(0,-1)),
    EAST (new LocationVector(0,1));

    private final LocationVector movementVector;

    private Direction(final LocationVector locationVector) {
        this.movementVector = locationVector;
    }

    public LocationVector getMovementVector() {
        return movementVector;
    }

    public Direction left() {
        switch (this) {
        case EAST:
            return Direction.NORTH;
        case NORTH:
            return Direction.WEST;
        case SOUTH:
            return Direction.EAST;
        case WEST:
            return Direction.SOUTH;
        }
        return null;
    }

    public Direction right() {
        switch (this) {
        case EAST:
            return Direction.SOUTH;
        case NORTH:
            return Direction.EAST;
        case SOUTH:
            return Direction.WEST;
        case WEST:
            return Direction.NORTH;
        }
        return null;
    }
}