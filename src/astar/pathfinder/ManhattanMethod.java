package astar.pathfinder;

import astar.info.Coordinate;
import astar.info.Grid;

/**
 * The manhattan method involves treating the path as a city grid similar to how Manhattan is laid out.
 * This method is not admissable, so it is not guaranteed to always find the shortest path, however, it
 * is much quicker than other algorithms, and finds paths that are close to the shortest.
 *
 * @author Jonathan Reimels
 *
 */
public class ManhattanMethod extends Astar {

    @Override
    protected int estimateDistance(Coordinate coord) {
        return (Math.abs(coord.getRow() - _grid.getEnd().getRow()) + Math.abs(coord.getCol() - _grid.getEnd().getCol())) * Grid.MOVE_LATERAL;
    }

    @Override
    public String toString() {
        return "Manhattan Method";
    }
}
