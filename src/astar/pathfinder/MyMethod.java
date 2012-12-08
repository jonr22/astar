package astar.pathfinder;

import astar.info.Coordinate;
import astar.info.Grid;

/**
 * The Diagonal method.
 *
 * @author Jonathan Reimels
 *
 */
public class MyMethod extends Astar {

    @Override
    protected int estimateDistance(Coordinate coord) {
    	int x = Math.abs(coord.getRow() - _grid.getEnd().getRow());
        int y = Math.abs(coord.getCol() - _grid.getEnd().getCol());
        int diff = Math.abs(x - y);
        
        if (diff % 2 == 0) {
        	return (diff / 2) * Grid.MOVE_LATERAL;
        }
        return ((diff + 1) / 2) * Grid.MOVE_LATERAL;
    }

    @Override
    public String toString() {
        return "My Method";
    }
}
