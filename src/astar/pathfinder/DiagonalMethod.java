package astar.pathfinder;

import astar.info.Coordinate;
import astar.info.Grid;

/**
 * The Diagonal method.
 *
 * @author Jonathan Reimels
 *
 */
public class DiagonalMethod extends Astar {

    @Override
    protected int estimateDistance(Coordinate coord) {
    	int x = Math.abs(coord.getRow() - _grid.getEnd().getRow());
        int y = Math.abs(coord.getCol() - _grid.getEnd().getCol());
        if (x > y) {
        	return Grid.MOVE_DIAGONAL * y + Grid.MOVE_LATERAL * (x - y);
        }
        return Grid.MOVE_DIAGONAL * x + Grid.MOVE_LATERAL * (y - x);
    }

    @Override
    public String toString() {
        return "Diagonal Method";
    }
}
