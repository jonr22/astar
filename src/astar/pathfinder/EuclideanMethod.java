package astar.pathfinder;

import astar.info.Coordinate;
import astar.info.Grid;

/**
 * The manhattan method involves treating the path as a city grid similar to how Manhattan is laid out
 *
 * @author Jonathan Reimels
 *
 */
public class EuclideanMethod extends Astar {

    @Override
    protected int estimateDistance(Coordinate coord) {
        int x = Math.abs(coord.getRow() - _grid.getEnd().getRow());
        int y = Math.abs(coord.getCol() - _grid.getEnd().getCol());
        int squared = x*x + y*y;
        int distance = (int)Math.floor(Math.sqrt(squared));
        return distance * Grid.MOVE_LATERAL;
    }

    @Override
    public String toString() {
        return "Euclidean Method";
    }
}
