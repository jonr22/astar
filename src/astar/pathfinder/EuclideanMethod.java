package astar.pathfinder;

import astar.info.Coordinate;
import astar.info.Grid;

/**
 * The Euclidean method determines the diagonal distance (a^2 + b^2)^(1/2) to the end node
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
