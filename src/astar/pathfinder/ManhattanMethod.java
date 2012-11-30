package astar.pathfinder;

import astar.info.Coordinate;
import astar.info.Grid;

/**
 * The manhattan method involves treating the path as a city grid similar to how Manhattan is laid out
 * 
 * @author Jonathan Reimels
 *
 */
public class ManhattanMethod extends Astar{
	
	protected int estimateDistance(Coordinate coord) {
		return (Math.abs(coord.getRow() - _grid.getEnd().getRow()) + Math.abs(coord.getCol() - _grid.getEnd().getCol())) * Grid.MOVE_LATERAL;
	}
	
	public String toString() {
		return "Manhattan Method";
	}
}
